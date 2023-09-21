package com.devdiegomata90.nueva_vida_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity



class InicioSesion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        actionBarpersonalizado("Inicio Session")


    }

    private fun actionBarpersonalizado(titulo: String){
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!! // CREAMOS EL ACTIONBAR
        actionBar!!.title = title // LE ASINAMOS UN TITULO
        actionBar!!.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}