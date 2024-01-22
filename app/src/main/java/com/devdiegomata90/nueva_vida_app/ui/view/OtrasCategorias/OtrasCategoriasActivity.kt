package com.devdiegomata90.nueva_vida_app.ui.view.OtrasCategorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.devdiegomata90.nueva_vida_app.databinding.ActivityOtrasCategoriasBinding
import com.devdiegomata90.nueva_vida_app.ui.adapter.OtrasCategoriasAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.OtrasCategoriasViewModel


class OtrasCategoriasActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtrasCategoriasBinding
    private val oCategoriasViewModel: OtrasCategoriasViewModel by viewModels() //Inicializa el ViewModel
    lateinit var adapterOCategorias:OtrasCategoriasAdapter
    private lateinit var categoriasDetalleList: List<CategoriaDetalle>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtrasCategoriasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recuperar informacion enviada por el intent
        val nombreOtraCategoria = getIntent().getStringExtra("NombreCategoria");

        actionBarpersonalizado(nombreOtraCategoria!!)

        initRecycleView()

        //Oncreate del viewModel
        oCategoriasViewModel.onCreate()

        //Para obtener la categoria selecionada
        oCategoriasViewModel.showCategorieDetail(nombreOtraCategoria)

        //Para mostrar la lista detalle de la categoria seleccionada
        oCategoriasViewModel.oCategories.observe(this, Observer { oCategories ->

            adapterOCategorias.updateData(oCategories as List<CategoriaDetalle>)
        })
    }

    //Metodo para crear un recicle view
    private fun initRecycleView(){
        categoriasDetalleList= emptyList()

        // Configurar el adaptador de la lista
       adapterOCategorias = OtrasCategoriasAdapter(
           CategoriaDetalleList = categoriasDetalleList,
           onClickListener = { categoriaDetalle -> getOCategories(categoriaDetalle) }
       )

        binding.rvOtrasCategorias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOtrasCategorias.adapter = adapterOCategorias

    }

    private fun getOCategories(categoriaDetalle: CategoriaDetalle) {
        Toast.makeText(this, "Haz dado click en ${categoriaDetalle.titulo}", Toast.LENGTH_SHORT).show()
    }


    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String){
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