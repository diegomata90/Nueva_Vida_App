package com.devdiegomata90.nueva_vida_app.ui.view.Evento

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.ui.view.EventoA.EventoAgregarActivity


class EventoActivity : AppCompatActivity() {
    private lateinit var crearEvento:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Evento")

        //Boton para enviar
        crearEvento= findViewById(R.id.CrearEvento)
        //Asignar Evento
        crearEvento.setOnClickListener { startActivity(Intent(this@EventoActivity,
            EventoAgregarActivity::class.java)) }

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
        return super.onNavigateUp()
    }
}