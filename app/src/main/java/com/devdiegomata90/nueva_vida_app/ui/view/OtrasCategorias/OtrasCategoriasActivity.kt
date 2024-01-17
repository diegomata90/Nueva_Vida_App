package com.devdiegomata90.nueva_vida_app.ui.view.OtrasCategorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.databinding.ActivityOtrasCategoriasBinding
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.BibliaViewModel
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.OtrasCategoriaViewModel

class OtrasCategoriasActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtrasCategoriasBinding
    private val oCategoriasViewModel: OtrasCategoriaViewModel by viewModels() //Inicializa el ViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtrasCategoriasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recuperar informacion enviada por el intent
        val nombreOtraCategoria = getIntent().getStringExtra("NombreCategoria");

        actionBarpersonalizado(nombreOtraCategoria!!)

        //Oncreate del viewModel
        oCategoriasViewModel.onCreate()
    }

    //Metodo para crear un recicle view
    private fun crearRecycleView(){
       //
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