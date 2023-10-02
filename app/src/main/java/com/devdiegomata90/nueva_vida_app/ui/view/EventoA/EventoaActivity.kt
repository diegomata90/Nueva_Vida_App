package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.devdiegomata90.nueva_vida_app.R


class EventoaActivity : AppCompatActivity() {

    lateinit var CrearEvento: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventoa)

        initComponent()
        iniUI()
        evento()

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Evento")

    }

    private fun initComponent() {
        CrearEvento = findViewById(R.id.CrearEvento)
    }

    private fun evento() {
        CrearEvento.setOnClickListener {
            startActivity(Intent(this, EventoAgregarActivity::class.java))
            finish()
        }
    }

    private fun iniUI() {

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