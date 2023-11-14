package com.devdiegomata90.nueva_vida_app.ui.view.Biblia

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.retrofit2.BooksApiServe
import com.devdiegomata90.nueva_vida_app.retrofit2.BooksResponse
import com.devdiegomata90.nueva_vida_app.retrofit2.VersesApiServe
import com.devdiegomata90.nueva_vida_app.retrofit2.VersiculoResponse
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.TextBiblicoAdapter
import com.devdiegomata90.nueva_vida_app.util.TypefaceUtil
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BibliaActivity : AppCompatActivity() {

    private lateinit var booksTV: AutoCompleteTextView
    private lateinit var chapterTV: AutoCompleteTextView
    private lateinit var inputBook: TextInputLayout
    private lateinit var inputChapter: TextInputLayout
    private lateinit var libro: TextView
    private lateinit var capitulo: TextView
    private lateinit var rvVersiculo: RecyclerView
    private lateinit var versiculoAdapter: TextBiblicoAdapter
    private lateinit var versiculoList: List<VersiculoResponse>
    private var IDBOOK:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblia)

        //Se agregar el actionBar personalizado
        actionBarPersonalizado("Biblia")

        initUI()
        initComponent()

        TypefaceUtil.asignarTipoLetra(
            this, null,
            booksTV,
            chapterTV,
            inputBook,
            inputChapter,
            libro,
            capitulo
        )

    }

    private fun initUI() {
        booksTV = findViewById(R.id.BooksTV)
        chapterTV = findViewById(R.id.ChapterTV)
        inputBook = findViewById(R.id.InputBook)
        inputChapter = findViewById(R.id.InputChapter)
        libro = findViewById(R.id.Libro)
        capitulo = findViewById(R.id.Capitulo)
        rvVersiculo = findViewById(R.id.rvVersiculo)

    }

    private fun initComponent() {
        showBook()
        initRecyclerView()

        //Escuchar los cambio en el lista de Capitulos
        chapterTV.doOnTextChanged { _, _, _, _ ->
            val selectedBook = booksTV.text.toString()
            val selectedChapter = chapterTV.text.toString()

            if (selectedBook.isNotEmpty() && selectedBook != getString(R.string.SeleccionTXT) && selectedChapter.isNotEmpty() && selectedChapter != getString(R.string.SeleccionTXT) ) {
                //Llamar a la funcion para mostrar el texto
                showVerse(selectedBook, selectedChapter, IDBOOK)
            }
        }

        //Inicializar con Génesis =>Capitulo 1
        showVerse("Génesis", "1", "spa-RVR1960:Gen")

    }

    private fun initRecyclerView() {
        versiculoList = emptyList()

        // Configurar el adaptador de la lista
        versiculoAdapter = TextBiblicoAdapter(
            versiculoList = versiculoList,
            onClickListener = { versiculo -> getVersiculo(versiculo) }
        )
        rvVersiculo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvVersiculo.adapter = versiculoAdapter
    }


    //Metodo para crear el retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Mostrar el libros
    private fun showBook() {
        val url = "https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/books/"

        //Corutina para ejecutar en el hilo secundario
        lifecycleScope.launch {
            val call = getRetrofit().create(BooksApiServe::class.java).getBooks("$url")

            try {

                //Valida si la respuesta es exitosa
                if (call.isSuccessful) {

                    //almacena la respuesta
                    val books = call.body() ?: emptyList()

                    //ordena y devuelve el nombre del libro
                    val bookName = books.sortedBy { it.order }.map{it.name}

                    //Llena lista de desplegable
                    val adapterBooks = ArrayAdapter( this@BibliaActivity, R.layout.item_dropdown,bookName)

                    booksTV.apply{
                        setAdapter(adapterBooks)

                        // Escucha cuando hay algun cambio el texto
                        doOnTextChanged { selectedBook, _, _, _ ->

                            //almacena libro seleccionado
                            val bookSelected = books.filter{ it.name == selectedBook.toString() }

                            Log.i("BOOK SELECT", "${bookSelected.map { it.name }}")

                            //llama funcion para mostrar los capitulos
                            showChapters(bookSelected)


                        }
                    }
                }

            } catch (e: Exception) {
                showError("showBook", e.message)
            } catch (e: HttpException) {
                showError("showBook", e.message)
            } catch (e: Throwable) {
                showError("showBook", e.message)
            }
        }
    }

    private fun showChapters(bookSelected: List<BooksResponse>) {

        //Borar valores del listade capitulos
        chapterTV.setText("")

        var totalChapter: MutableList<String> = mutableListOf()

        //almacena los caputlos del libro
        bookSelected.flatMap { it.capitulos  }.mapTo(totalChapter) { it.n_chapter.toString() }

        //Mostrar los Capitulos
        val adapterChapter = ArrayAdapter(this@BibliaActivity, R.layout.item_dropdown, totalChapter)

        IDBOOK = bookSelected.map { it.version_book }.first().toString()


        chapterTV.apply {
            setAdapter(adapterChapter)
        }
    }

    private fun showVerse(book: String, chapter: String, version: String) {

        val Url = "https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/books/"
        val bookChapter = "$version/verses/$chapter"


        // Utiliza Uri.Builder para construir la URL de manera segura
        val fullUrlV = Uri.parse(Url).buildUpon()
            .appendEncodedPath(bookChapter)
            .build().toString()

        // Log para verificar la URL antes de la llamada a la API
        Log.i("showVerse URL2", "URL de la API: $fullUrlV")

        //Corutina para mostrar Versiculo en el recicleView
        lifecycleScope.launch() {
            val call = getRetrofit().create(VersesApiServe::class.java).getVerse("$fullUrlV")

            try {
                val verses: List<VersiculoResponse>? = call.body()

                if (call.isSuccessful) {

                    //Mostra nombre libro y capitulo
                    libro.text = book
                    capitulo.text = chapter

                    //ordena la lista
                    val versesSort = verses?.sortedBy { it.codigo }

                    // Log para verificar los datos recuperados antes de mostrarlos
                    Log.i(
                        "showVerse SORT", "Versículos recuperados: ${
                            versesSort?.map { it.capitulo }?.first()
                                .toString() + "  " + versesSort?.map { it.cleanText }
                                ?.first()
                                .toString()
                        }"
                    )

                    versiculoList = versesSort ?: emptyList()

                    // Actualiza los datos en el adaptador después de llenar la lista
                    versiculoAdapter.updateData((versiculoList as List<VersiculoResponse>))

                    Log.i(
                        "showVerse Resultado",
                        versiculoList.map { it.capitulo }.first()
                            .toString() + "  " + versiculoList.map { it.cleanText }.first()
                            .toString()
                    )

                } else {
                    showError("showVerse", call.message())
                }

            } catch (e: Exception) {
                showError("showVerse", e.message)
            }
        }
    }

    //Metodo para compartir versiculo (Seleccionado del reciclerView)
    private fun getVersiculo(versiculo: VersiculoResponse) {
        Toast.makeText(this, "Versiculo ${versiculo.cleanText}", Toast.LENGTH_SHORT).show()
    }

    //Muestra un error en caso de que se produzca
    private fun showError(method: String?, additionalMessage: String?) {
        try {
            throw RuntimeException("Error al ejecutar el API $method. Detalles: $additionalMessage")
        } catch (e: RuntimeException) {
            Log.e("Error ==>> ", e.message, e)
            Toast.makeText(this, "Error inesperado en el metodo $method", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actionBarPersonalizado(titulo: String) {
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!!          // CREAMOS EL ACTIONBAR
        actionBar.title = titulo                   // LE ASINAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}