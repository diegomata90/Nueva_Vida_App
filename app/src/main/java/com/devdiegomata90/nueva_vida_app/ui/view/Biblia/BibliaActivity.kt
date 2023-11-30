package com.devdiegomata90.nueva_vida_app.ui.view.Biblia

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.LoadingDialog
import com.devdiegomata90.nueva_vida_app.databinding.ActivityBibliaBinding
import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse
import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse
import com.devdiegomata90.nueva_vida_app.ui.adapter.TextBiblicoAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.BibliaViewModel
import kotlinx.coroutines.launch


class BibliaActivity : AppCompatActivity() {


    private lateinit var binding: ActivityBibliaBinding //declara el Binding
    private lateinit var loadingDialog: LoadingDialog
    private val bibliaViewModel: BibliaViewModel by viewModels() //Inicializa el ViewModel
    private lateinit var versiculoList: List<VersesResponse>
    private lateinit var versiculoAdapter: TextBiblicoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBibliaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarPersonalizado("Biblia RVR1960")

        initComponent()


        bibliaViewModel.onCreate()

        //Suscribe la ViewModel
        bibliaViewModel.books.observe(this, Observer { books ->
            showBook(books)
        })

        bibliaViewModel.chapters.observe(this, Observer { chapters ->
            showChapter(chapters)
        })

        bibliaViewModel.verses.observe(this, Observer { verses ->
            showVerses(verses)
        })

        bibliaViewModel.bookAndChapter.observe(this, Observer { bookAndChapter ->

            //Mostra nombre libro y capitulo
            binding.Libro.text = bookAndChapter.bookName
            binding.Capitulo.text = bookAndChapter.chapterNumber

        })


        bibliaViewModel.loading.observe(this, Observer { loading ->

            if (loading) {
                loadingDialog.starLoading()
            } else {
                loadingDialog.isDismiss()
            }
        })

    }

    private fun initComponent() {

        // Inicializa la instancia del LoadingDialog
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Cargando..."
        loadingDialog.setCancelable = false

        initRecyclerView()

    }

    private fun showBook(books: List<BooksResponse>?) {

        //Validar si viene vacia
        if (books != null) {

            //ordena y devuelve solo el nombre del libro
            val bookName = books.map { it.name }

            //Crear el adaptador para lista desplegable
            val adapter = ArrayAdapter(this@BibliaActivity, R.layout.item_dropdown, bookName)

            //Asigna el adaptador
            binding.BooksTV.apply {
                setAdapter(adapter)

                doOnTextChanged { bookSelected, _, _, _ ->
                    //Llamar el metodo para mostrar los chapters
                    lifecycleScope.launch {
                        //llama a la funcion del viewmodel
                        bibliaViewModel.showChapters(bookSelected = bookSelected.toString())
                        binding.ChapterTV.setText("")
                    }
                }
            }
        }
    }

    private fun showChapter(bookSelected: List<String>?) {

        //Validar si viene vacia
        if (bookSelected != null) {
            //Borar valores del listade capitulos
            //binding.ChapterTV.setText("")

            //Crear el adaptador para lista desplegable
            val adapter = ArrayAdapter(this@BibliaActivity, R.layout.item_dropdown, bookSelected)

            //Asigna el adaptador
            binding.ChapterTV.apply {
                setAdapter(adapter)

                doOnTextChanged() { chapterSelected, _, _, _ ->

                    lifecycleScope.launch {
                        //llama a la funcion del viewmodel
                        bibliaViewModel.showVerse(
                            bookSelected = binding.BooksTV.text.toString(),
                            chapterSelected = chapterSelected.toString()

                        )
                    }

                }

            }
        }
    }

    private fun showVerses(verses: List<VersesResponse>?) {

        //Validar si viene vacia
        if (verses != null) {

            versiculoList = verses

            // Actualiza los datos en el adaptador después de llenar la lista
            versiculoAdapter.updateData((versiculoList))

        }

    }

    private fun initRecyclerView() {
        versiculoList = emptyList()

        // Configurar el adaptador de la lista
        versiculoAdapter = TextBiblicoAdapter(
            versiculoList = versiculoList,
            onClickListener = { versiculo -> getVersiculo(versiculo) }
        )
        binding.rvVersiculo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVersiculo.adapter = versiculoAdapter
    }

    //Se crea un alert dialog para preguntar desea compartir el versiculo
    private fun getVersiculo(versiculo: VersesResponse) {

        //se Crea un alert dialogo para con un boton para compartir
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Compartir ")
        builder.setMessage("${versiculo.capitulo} RVR1960\n${versiculo.cleanText}")
            .setPositiveButton("Compartir") { _, _ ->
                val message =
                    "${versiculo.capitulo} RVR1960\n${versiculo.cleanText}\n\n${getText(R.string.app_name)}"

                //llama a la funcion del viewmodel
                bibliaViewModel.shareText(this@BibliaActivity, message)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()

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