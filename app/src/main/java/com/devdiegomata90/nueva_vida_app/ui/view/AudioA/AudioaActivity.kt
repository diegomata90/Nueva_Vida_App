package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.R
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

        // Observa cambios en el StateFlow de los Audios
        lifecycleScope.launch {
            audioAdminViewModel.audios.collect { audios ->
                // Recupera los audios y agragar la lista al reciclerView
               audioAdapter.updateData(audios as List<Audio>)
            }
        }

        //Mostrar el mensaje de borrado
        audioAdminViewModel.message.observe(this) {message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }


    }


    private fun initReciclerView() {
        audioList = emptyList()

        // Configurar el adaptador de la lista
        audioAdapter = AudioAdapter(
            audios = audioList,
            onClickListener = { audio -> action(audio) },
            admin = true
        )

        binding.rvAudio.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAudio.adapter = audioAdapter


    }

    private fun action(audio: Audio){

        // Mostrar las opciones
        val opciones = arrayOf("Actualizar", "Eliminar")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona la acción")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        // "Selecionando: Actualizar"
                        onUpdateAudioClick(audio)
                    }
                    1 -> {
                        //"Selecionando: Eliminar"
                        showDeleteConfirmation(audio)

                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun onUpdateAudioClick(audio: Audio) {
        // "Selecionando: Actualizar"
        Toast.makeText(this, "Actualizar ${audio.titulo}", Toast.LENGTH_SHORT).show()
    }

    //Dialogo de Confirmacion de eliminacion
    private fun showDeleteConfirmation(audio: Audio) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este elemento?")
            .setPositiveButton("Sí") { _, _ ->
                audioAdminViewModel.deleteAudio(audio)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }




    //Agregarmo el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater // CREO OBJETO TIPO MenuInflater
        menuInflater.inflate(
            R.menu.menu_agregar,
            menu
        ) // INFLO EL MENU CREADO EN LLAMADO MENU_AGREGAR

        return super.onCreateOptionsMenu(menu)
    }

    //Direciona el menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.agregar -> {
                startActivity(Intent(this@AudioaActivity, AudioAgregarActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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