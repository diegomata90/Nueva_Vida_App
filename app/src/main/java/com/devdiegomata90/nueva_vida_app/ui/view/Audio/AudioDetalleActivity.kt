package com.devdiegomata90.nueva_vida_app.ui.view.Audio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioListAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.ArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioDetalleActivity() : AppCompatActivity() {

    private lateinit var audioList: List<Audio>
    private lateinit var audioPlayer: MediaPlayer
    private lateinit var rvAudiolist: RecyclerView
    private lateinit var audioAdapter: AudioListAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var playButton: android.widget.ImageView
    private lateinit var pauseButton: android.widget.ImageView
    private lateinit var progressSeekBar: SeekBar
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var imagenCovered: android.widget.ImageView
    private lateinit var tituloAudioReproduct: TextView
    private lateinit var audioTimeStart: TextView
    private lateinit var audioTimeEnd: TextView
    private var isPlaying: Boolean = false // Variable de estado
    private val uiScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_detalle)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Lista Reproducción")

        initUID() //Inicator de la intenfance
        initComponent() // Vincular los componentes
        eventosBotones()
    }


    private fun initUID() {
        rvAudiolist = findViewById(R.id.audio_list_recycler_view)
        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        progressSeekBar = findViewById(R.id.progress_seek_bar)
        volumeSeekBar = findViewById(R.id.volume_seek_bar)
        imagenCovered = findViewById(R.id.imagen_Covered)
        tituloAudioReproduct = findViewById(R.id.TituloAudioReproduct)
        audioTimeStart = findViewById(R.id.audio_time_start)
        audioTimeEnd = findViewById(R.id.audio_time_end)

    }

    private fun eventosBotones() {

        // Set the onClickListener of the play button
        playButton.setOnClickListener {
            // Start playing the audio
            audioPlayer.start()
            isPlaying = true
            // Asegúrate de estar en una coroutine al llamar a la función suspendida
            uiScope.launch {
                updateSeekBar()
            }

            adminBoton()
        }

        // Set the onClickListener of the pause button
        pauseButton.setOnClickListener {
            // Pause the audio
            audioPlayer.pause()
            isPlaying = false

            adminBoton()

        }

        // Configurar el control de volumen
        volumeSeekBar.max = 100
        volumeSeekBar.progress = 60

        // Vincular los eventos del control de volumen al reproductor de audio
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioPlayer.setVolume(progress.toFloat(), progress.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Vincular los eventos del control de progreso al reproductor de audio
        progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Si el usuario cambió la posición del SeekBar, actualiza la posición del audio
                    audioPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Detén la actualización del SeekBar mientras el usuario interactúa con él
                audioPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Cuando el usuario suelta el SeekBar, reanuda la reproducción y la actualización del SeekBar
                audioPlayer.start()
                uiScope.launch {
                    updateSeekBar()
                }
            }
        })


    }

    private fun initComponent() {
        databaseReference = FirebaseDatabase.getInstance().getReference("AUDIOS")

        // Obtener la lista de archivos de audio
        audioList = getAudioList()

        // Configurar el reproductor de audio
        audioPlayer = MediaPlayer()

        rvAudiolist = findViewById(R.id.audio_list_recycler_view)
        audioAdapter = AudioListAdapter(
            audioList = audioList,
            onClickListener = { audio -> playAudio(audio) }
        )
        rvAudiolist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAudiolist.adapter = audioAdapter

        // Configura el máximo del SeekBar
        progressSeekBar.max = audioPlayer.duration


    }


    private fun getAudioList(): List<Audio> {
        val query =
            databaseReference.orderByChild("titulo")// Ordenar por el campo "titulo" si es necesario

        val audioList = ArrayList<Audio>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                audioList.clear()
                for (snapshot in dataSnapshot.children) {
                    val audio = snapshot.getValue(Audio::class.java)
                    if (audio != null) {
                        audioList.add(audio)
                    }
                }
                // Actualiza los datos en el adaptador después de llenar la lista
                audioAdapter.updateData(audioList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    this@AudioDetalleActivity,
                    "Error en Firebase: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return audioList
    }

    private fun playAudio(audio: Audio) {
        // Detener la reproducción si ya está en curso
        if (isPlaying) {
            audioPlayer.stop()
        }

        audioPlayer.reset()

        // Establecer el nuevo archivo de audio
        audioPlayer.setDataSource(audio.url)

        // Establecer audioTimeStart en cero
        audioTimeStart.text = "00:00"


        // Setear los titulos y tiempo del audio en reproduccion
        tituloAudioReproduct.text = audio.titulo

        // Montarla la imagen en el imagenView
        try {
            Picasso.get().load(audio.imagen).into(imagenCovered)
        } catch (e: Exception) {
            Picasso.get().load(R.drawable.categoria).into(imagenCovered)
        }

        audioPlayer.prepare()
        progressSeekBar.max = audioPlayer.duration // Establecer la duración del SeekBar


        audioPlayer.start()
        isPlaying = true

        // Establecer audioTimeEnd en total
        val totalDuration = audioPlayer.duration
        val remainingMinutes = totalDuration / 1000 / 60
        val remainingSeconds = totalDuration / 1000 % 60

        audioTimeEnd.text = String.format("%02d:%02d", remainingMinutes, remainingSeconds)

        // Start the Coroutine to update the SeekBar
        uiScope.launch {
            updateSeekBar()
        }
        adminBoton()
    }

    private suspend fun updateSeekBar() {
        val totalDuration = audioPlayer.duration
        while (audioPlayer.isPlaying) {
            val currentPosition = audioPlayer.currentPosition
            val currentMinutes = currentPosition / 1000 / 60
            val currentSeconds = currentPosition / 1000 % 60

            runOnUiThread {
                progressSeekBar.max = totalDuration
                progressSeekBar.progress = currentPosition
                audioTimeStart.text = String.format("%02d:%02d", currentMinutes, currentSeconds)
            }
            delay(1000) // Delay for 1 second
        }
    }

    //Metodo que muestra oculta los botones de play y pause
    private fun adminBoton() {

        if (isPlaying) {
            pauseButton.visibility = android.view.View.VISIBLE
            playButton.visibility = android.view.View.GONE
        } else {
            playButton.visibility = android.view.View.VISIBLE
            pauseButton.visibility = android.view.View.GONE
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

    override fun onPause() {
        super.onPause()
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
        audioPlayer.release()
    }

}

