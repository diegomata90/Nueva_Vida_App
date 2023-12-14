package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.databinding.ActivityAudioAgregarBinding
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAdminViewModel
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAgregarViewModel
import java.util.*

class AudioAgregarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioAgregarBinding
    private val audioAgregarViewModel: AudioAgregarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioAgregarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Audio")

        //
        eventoBoton()

        //Inicializa la funcion Oncreate para el viewModel
        audioAgregarViewModel.Oncreate()

        //Suscribirse al ViewModel
        //Mostrar el mensaje de borrado
        audioAgregarViewModel.message.observe(this) {message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }


    }


    private fun eventoBoton(){
        binding.BotonAudio.setOnClickListener {
            val audioAgregar = Audio()

            audioAgregar.titulo = binding.TituloAudio.text.toString()
            audioAgregar.descripcion = binding.DescripcionAudio.text.toString()
            audioAgregar.imagen = ""
            audioAgregar.url = ""

            //Obtener la fecha de hoy
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)
            val fecha = "$day-$month-$year"

            audioAgregar.fecha = fecha


            audioAgregarViewModel.addAudio(
                audioAgregar
            )
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

    //Regresar al actividad anterior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}

