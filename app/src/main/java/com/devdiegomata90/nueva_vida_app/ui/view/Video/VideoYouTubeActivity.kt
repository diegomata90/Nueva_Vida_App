package com.devdiegomata90.nueva_vida_app.ui.view.Video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.devdiegomata90.nueva_vida_app.databinding.ActivityVideoYouTubeBinding
import com.devdiegomata90.nueva_vida_app.ui.adapter.VideoAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.VideoViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class VideoYouTubeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoYouTubeBinding
    private val videoViewModel: VideoViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var IdVideo: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoYouTubeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Inicializar la barra de menu
        actionBarpersonalizado("Videos")

        //Inicializa el ReciclerView
        initReciclerView()


        //oncreate viewModel
        videoViewModel.onCreate()

        //Inicializo el observer
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                //Observar los cambio en la lista de videos para cargar
                observerVideo()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                youTubePlayer?.pause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                youTubePlayer?.play()
            }


            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                youTubePlayer?.pause()
                binding.youtubePlayerView.release()
            }
        })
    }

    // Observa cambios en el StateFlow de los Videos
    private fun observerVideo() {
        // Observa cambios en el StateFlow de los Videos para cargar la lista
        lifecycleScope.launch {
            videoViewModel.videos.collect { videos ->
                // Recupera los videos y agragar la lista al reciclerView
                videoAdapter.updateData(videos as List<Video>)

                // Obtiene el primer link si la lista no está vacía
                if (videos.isNotEmpty()) {
                    IdVideo = videos.first().IDYOUTUBE.toString()
                    //Inicializar el reproductor
                    initializeYoutubePlayer(IdVideo)
                }
            }
        }
    }

    private fun initializeYoutubePlayer(idVideo: String) {
        // inicializa el reproductor
        // documentacion : https://github.com/PierfrancescoSoffritti/android-youtube-player?tab=readme-ov-file#sample-app

        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubePlayer?.let {
                    // cargar el video sin reproducirlo
                    it.cueVideo("$idVideo", 0f)
                    // cargar el video y lo reproduce
                    //it.loadVideo("$idVideo", 0f)
                }
            }
        })
    }

    private fun initReciclerView() {

        videoAdapter = VideoAdapter(
            videos = emptyList(),
            onClickListener = { video -> showVideo(video) }
        )

        binding.rvVideo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVideo.adapter = videoAdapter

    }

    // muestra el video seleccionado
    private fun showVideo(video: Video) {

        IdVideo = video.IDYOUTUBE.toString()

        Toast.makeText(this, "Video: ${video.TITULO}", Toast.LENGTH_SHORT)
            .show()

        // carga el video selecionado
        youTubePlayer?.loadVideo(IdVideo, 0f)
    }


    private fun actionBarpersonalizado(titulo: String) {
        val actionBar = supportActionBar!!

        actionBar.apply {
            title = titulo
           setDisplayHomeAsUpEnabled(true)
           // setDisplayShowTitleEnabled(true)
           // setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return super.onSupportNavigateUp()
    }

    //Pausa y destroye el reproductor para liberar recursos
    override fun onDestroy() {
        super.onDestroy()
        youTubePlayer?.pause()
        binding.youtubePlayerView.release()
    }

}


