package com.devdiegomata90.nueva_vida_app.ui.view.Audio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.model.Audio
import com.devdiegomata90.nueva_vida_app.ui.adapter.AudioListAdapter
import com.devdiegomata90.nueva_vida_app.util.TypefaceUtil
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.util.*


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
    private lateinit var audioListaTXT: TextView
    private var isPlaying: Boolean = false
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var audioRecibido: Audio


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_detalle)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Lista Reproducción")


        initUID() //Inicator de la intenfance
        initComponent() // Vincular los componentes
        eventosBotones() //Eventos de los botones

        //Recibir los datos del intent para reproducir el audio
        obtenerAudio()

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disenada para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(this,
            null,
            tituloAudioReproduct,
            audioTimeStart,
            audioTimeEnd,
            audioListaTXT
        )
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
        audioListaTXT = findViewById(R.id.AudioListaTXT)

    }

    private fun eventosBotones() {

        // Set the onClickListener of the play button
        playButton.setOnClickListener {
            // Start playing the audio
            audioPlayer.start()
            isPlaying = true
            // Coroutine para llamar a la función suspendida
            uiScope.launch {
                updateSeekBar()
            }
            // Metodo para cambiar el boton play a pause y viseversa
            adminBoton()
        }

        // Set the onClickListener of the pause button
        pauseButton.setOnClickListener {
            // Pause the audio
            audioPlayer.pause()
            isPlaying = false
            // Metodo para cambiar el boton play a pause y viseversa
            adminBoton()

        }

        // Vincular los eventos del control de volumen al reproductor de audio
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { // Si el usuario cambia el valor del SeekBar
                    val maxVolume = 100 // Valor máximo del volumen
                    val volume = progress.toFloat() / maxVolume // Normalizar el valor del volumen
                    audioPlayer.setVolume(volume, volume) // Establecer el volumen
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
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

        // Comprobar si la variable audioPlayer es nula
        audioPlayer = if (audioPlayer == null) {
            // Inicializar la variable audioPlayer
            MediaPlayer.create(this, R.raw.audio_muestra)
        } else {
            audioPlayer
        }


        // Configurar el adaptador de la lista
        rvAudiolist = findViewById(R.id.audio_list_recycler_view)
        audioAdapter = AudioListAdapter(
            audioList = audioList,
            onClickListener = { audio -> playAudio(audio) }
        )

        // Configurar el RecyclerView
        rvAudiolist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAudiolist.adapter = audioAdapter

        // Configura el máximo del SeekBar
        progressSeekBar.max = audioPlayer.duration


        // Configurar el control de volumen
        volumeSeekBar.max = 100
        volumeSeekBar.progress = 70


    }

    private fun obtenerAudio() {
        //validar si y setearlo audio que recibo del intent
        val intent = intent.extras

        //se inicializar el contenedor de los campos del intent a recibir
        audioRecibido = Audio()

        //Valida si el intent no es nulo
        if (intent != null) {
            audioRecibido.id = intent.getString("id")
            audioRecibido.titulo = intent.getString("titulo")
            audioRecibido.imagen = intent.getString("imagen")
            audioRecibido.url = intent.getString("url")
            audioRecibido.descripcion = intent.getString("descripcion")
            audioRecibido.fecha = intent.getString("fecha")

            //Reproducir el audio selecionado
            playAudio(audioRecibido)
        }
    }

    // Metodo para obtener la lista de archivos de audio
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

    //Metodo para reproducir el audio
    private fun playAudio(audio: Audio) {
        // Detener la reproducción si ya está en curso
        if (isPlaying) {
            audioPlayer.stop()
        }

        audioPlayer.reset()

        // Establecer el nuevo archivo de audio
        audioPlayer.setDataSource(audio.url)

        // Restablecer el máximo y la posición del SeekBar
        progressSeekBar.max = 0
        progressSeekBar.progress = 0

        // Establecer audioTimeStart en cero
        audioTimeStart.text = "00:00"

        // Setear los titulos y tiempo del audio en reproduccion
        tituloAudioReproduct.text = audio.titulo

        // Montarla la imagen en el imagenView
        try {
            Picasso.get().load(audio.imagen).into(imagenCovered)
        } catch (e: Exception) {
            Picasso.get().load(R.drawable.album).into(imagenCovered)
        }

        audioPlayer.prepare()
        progressSeekBar.max = audioPlayer.duration // Establecer la duración del SeekBar


        audioPlayer.start()
        isPlaying = true

        // Establecer audioTimeEnd en total
        val totalDuration = audioPlayer.duration
        val totalHours = totalDuration / 1000 / 3600
        val totalMinutes = totalDuration / 1000 / 60 % 60
        val totalSeconds = totalDuration / 1000 % 60

        val formatoTime = if (totalHours > 0){
            String.format("%02d:%02d:%02d", totalHours,totalMinutes, totalSeconds)
        }else{
            String.format("%02d:%02d", totalMinutes, totalSeconds)
        }

        audioTimeEnd.text = formatoTime

        // Iniciar la actualización del SeekBar
        uiScope.launch {
            updateSeekBar()
        }

        adminBoton()
    }

    //Metodo que actualiza el SeekBar
    private suspend fun updateSeekBar() {
        while (audioPlayer.isPlaying) {
                    val currentPosition = audioPlayer.currentPosition
                    val currentHours = currentPosition / 1000 / 3600
                    val currentMinutes = currentPosition / 1000 / 60 % 60
                    val currentSeconds = currentPosition / 1000 % 60

                    runOnUiThread {
                        var formatoTiempo = if (currentHours > 0) {
                            String.format("%02d:%02d:%02d", currentHours, currentMinutes, currentSeconds)
                        } else {
                            String.format("%02d:%02d", currentMinutes, currentSeconds)
                        }

                        progressSeekBar.progress = currentPosition
                        audioTimeStart.text = formatoTiempo

                        // Actualiza el estado de los botones play y pause según el estado actual del reproductor de audio
                        adminBoton()
                    }
            delay(1000) // Delay for 1 second
        }
    }


    //Metodo que muestra oculta los botones de play y pause
    private fun adminBoton() {

        if (audioPlayer.isPlaying) {
            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
        } else {
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.stop()
    }

}

