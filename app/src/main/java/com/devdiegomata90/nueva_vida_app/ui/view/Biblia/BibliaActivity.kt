package com.devdiegomata90.nueva_vida_app.ui.view.Biblia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.Toast.*
import androidx.core.widget.doOnTextChanged
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.retrofit2.BooksApiServe
import com.devdiegomata90.nueva_vida_app.retrofit2.BooksResponse
import com.devdiegomata90.nueva_vida_app.util.TypefaceUtil
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BibliaActivity : AppCompatActivity() {

    private lateinit var booksTV: AutoCompleteTextView
    private lateinit var chapterTV: AutoCompleteTextView
    private lateinit var inputBook: TextInputLayout
    private lateinit var inputChapter: TextInputLayout
    private lateinit var libro:TextView
    private lateinit var capitulo:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblia)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Biblia")

        initComponent()
        initUi()

        TypefaceUtil.asignarTipoLetra(this, null, booksTV, chapterTV, inputBook, inputChapter,libro,capitulo)

    }

    private fun initComponent() {
        booksTV = findViewById(R.id.BooksTV)
        chapterTV = findViewById(R.id.ChapterTV)
        inputBook = findViewById(R.id.InputBook)
        inputChapter = findViewById(R.id.InputChapter)
        libro = findViewById(R.id.Libro)
        capitulo = findViewById(R.id.Capitulo)
    }


    private fun initUi() {
        showBook()
    }


    //Metedo para crear el retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    //Metodo para mostrar el dropdown Libros
    private fun showBook() {
        val url = "https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/books/"


        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(BooksApiServe::class.java).getBooks("$url")
            val books: List<BooksResponse>? = call.body()

            runOnUiThread() {
                if (call.isSuccessful) {
                    val books = call.body() ?: emptyList()

                    // Ordena y devuelve el nombre del libro
                    val booksName = books.sortedBy { it.order }.map { it.name }

                    val adapterBooks = ArrayAdapter(
                        this@BibliaActivity,
                        R.layout.item_dropdown, // este item XML diseñado para el dropdown
                        booksName // lista de los books del api
                    )
                    booksTV.apply {
                        setAdapter(adapterBooks)

                        doOnTextChanged { selectedBooks, _, _, _ ->


                            val bookSelected = books.filter { it.name == selectedBooks.toString() }

                            //Envia el total de los capitulos
                            showChapters(bookSelected)
                            limpiarTextoBiblico()
                        }
                    }
                } else {
                    showError()
                }
            }

        }


    }

    //Muestra un error
    private fun showError() {
        makeText(this, "Error al ejecutar el api", LENGTH_SHORT).show()

    }

    //LLena la lista de Capitulos
    private fun showChapters(bookSelected: List<BooksResponse>) {
        //Limpiar lista de capitulos
        chapterTV.setText("")

        var totalChapter: MutableList<String> = mutableListOf()

        bookSelected
            .flatMap { it.capitulos }
            .mapTo(totalChapter) { it.n_chapter.toString() }

        //Mostrar los Capitulos
        val adapterChapter = ArrayAdapter(this@BibliaActivity, R.layout.item_dropdown, totalChapter)

        chapterTV.apply {
            setAdapter(adapterChapter)

            doOnTextChanged { selectedChapter, _, _, _ ->

                    //Muestra el versiculo en el textView
                    showText(bookSelected, selectedChapter.toString())
                    Log.i("capitulo", getString(R.string.SeleccionTXT))
            }
        }

       // Log.i("capitulos", listChapters.toString())
    }

    //Metodo muestre el texto en la biblia
    private fun showText(bookSelected: List<BooksResponse>, selectedChapter: String) {

        //Mostra nombre libro y capitulo
        libro.text = bookSelected.map{ it.name}.first().toString()
        capitulo.text = selectedChapter

        //Mostrar Texto biblico
        showVersiculo()

    }

    //Mostrar Texto biblico
    private fun showVersiculo() {

    }


    private fun limpiarTextoBiblico(){
        //Limpiar lista de capitulos
        libro.text = ""
        capitulo.text = ""


    }


    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String) {
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


