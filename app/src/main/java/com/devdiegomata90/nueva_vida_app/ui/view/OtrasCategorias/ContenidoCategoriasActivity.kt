package com.devdiegomata90.nueva_vida_app.ui.view.OtrasCategorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.devdiegomata90.nueva_vida_app.databinding.ActivityContenidoCategoriasBinding
import com.squareup.picasso.Picasso

class ContenidoCategoriasActivity : AppCompatActivity() {

    lateinit var binding: ActivityContenidoCategoriasBinding
    lateinit var titulo: String
    lateinit var contenido: String
    lateinit var autor: String
    lateinit var imagen: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContenidoCategoriasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recuperar informacion enviada por el intent
        getDataInten()

        //Se agregar el actionBar personalizado
        actionBarpersonalizado(titulo)

        //Asignacion de los valores en los XML
        initComponent()

    }

    private fun getDataInten() {

        //Recuperar informacion enviada por el intent
        titulo = getIntent().getStringExtra("Titulo").toString()
        autor = getIntent().getStringExtra("Autor").toString()
        imagen = getIntent().getStringExtra("Imagen").toString()
        contenido = getIntent().getStringExtra("Contenido").toString()

    }

    private fun initComponent() {

        binding.tituloOCategoria.text = titulo
        binding.ContenidoOtrasCategorias.text = contenido
        binding.AutorOCategoria.text = autor

        //Asignar la imagen de la categoria
        //Setear o asignar la imagen al XML
        try {
            //Asignar la imagen en la image viewer
            Picasso.get().load(imagen).into(binding.imageOCategoria)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.image_ico).into(binding.imageOCategoria)
        }


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
