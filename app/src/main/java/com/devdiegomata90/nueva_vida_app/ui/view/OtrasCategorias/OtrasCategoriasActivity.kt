package com.devdiegomata90.nueva_vida_app.ui.view.OtrasCategorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devdiegomata90.nueva_vida_app.R

class OtrasCategoriasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otras_categorias)

        //Recuperar informacion enviada por el intent
        val nombreOtraCategoria = getIntent().getStringExtra("NombreCategoria");

        actionBarpersonalizado(nombreOtraCategoria!!)
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