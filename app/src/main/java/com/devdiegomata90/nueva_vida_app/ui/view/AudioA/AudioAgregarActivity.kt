package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.databinding.ActivityAudioaBinding

class AudioAgregarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Agregar Audio")
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