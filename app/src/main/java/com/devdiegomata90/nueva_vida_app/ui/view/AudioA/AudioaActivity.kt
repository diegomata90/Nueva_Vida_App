package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.databinding.ActivityAudioaBinding
import com.devdiegomata90.nueva_vida_app.ui.adapter.AudioAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAdminViewModel
import kotlinx.coroutines.launch

class AudioaActivity : AppCompatActivity() {

    private val audioAdminViewModel: AudioAdminViewModel by viewModels()
    private lateinit var binding: ActivityAudioaBinding

    private lateinit var audioAdapter: AudioAdapter
    private lateinit var audioList: List<Audio>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Audio Administrador")

        //Inicializa el ReciclerView
        initReciclerView()


        //Inicializa la funcion Oncreate para el viewModel
        audioAdminViewModel.Oncreate()

        // Observa cambios en el StateFlow de Audios
        // Recupera los audios y agragar la lista al reciclerView
        lifecycleScope.launch {
            audioAdminViewModel.audios.collect { audios ->
               audioAdapter.updateData(audios as List<Audio>)
            }
        }


    }

    private fun initReciclerView() {
        audioList = emptyList()

        // Configurar el adaptador de la lista
        audioAdapter = AudioAdapter(
            audios = audioList,
            onClickListener = { audio -> modificarAudio(audio) }
        )

        binding.rvAudio.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAudio.adapter = audioAdapter


    }



    private fun modificarAudio(audio: Audio) {
        // Logica para modificar audio
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