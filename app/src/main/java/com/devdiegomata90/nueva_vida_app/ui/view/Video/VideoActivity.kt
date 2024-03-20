package com.devdiegomata90.nueva_vida_app.ui.view.Video

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.data.model.Video

import com.devdiegomata90.nueva_vida_app.databinding.ActivityVideoBinding
import com.devdiegomata90.nueva_vida_app.ui.adapter.VideoAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.VideoViewModel
import kotlinx.coroutines.launch


class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private val videoViewModel: VideoViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var linkP:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Inicializar la barra de menu
        actionBarpersonalizado()

        //Inicializa el ReciclerView
        initReciclerView()

        //Incializa el ViewModel
        videoViewModel.onCreate()


        // Observa cambios en el StateFlow de los Videos
        lifecycleScope.launch {
            videoViewModel.videos.collect { videos ->
                // Recupera los videos y agragar la lista al reciclerView
                videoAdapter.updateData(videos as List<Video>)

                // Obtiene el primer link si la lista no está vacía
                linkP = if (videos.isNotEmpty()) {
                    videos.first().LINK.toString()
                } else {
                    // En caso de que la lista esté vacía, proporciona un valor de respaldo
                    "https://www.youtube.com/" // Valor de respaldo
                }
                Log.d("FVIDEOSV", "VIDEOS: ${videos}, \n LINK: $linkP")
                initWebView()
            }
        }

        initWebView()


    }

    private fun initReciclerView() {
        //Crea una lista vacia
        val videoList = emptyList<Video>()

        //Crea el adapter
        videoAdapter = VideoAdapter(
            videoList,
            onClickListener = {video->
                //Funcion del evento
                mostrarVideo(video)
            }
        )

        //Asigna el layout
        binding.rvVideo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVideo.adapter = videoAdapter

    }

    private fun initWebView() {

        val link = if(videoViewModel.videos.value.size > 0) videoViewModel.videos.value.get(0).LINK.toString() else "https://www.youtube.com/"
        val videoYT = "<iframe width=\"100%\" height=\"100%\" src=\"$link\" " +
                "title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; " +
                "clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";


        binding.webViewVideoGENERAL.loadData(videoYT, "text/html; charset=utf-8", "utf-8")
        binding.webViewVideoGENERAL.settings.javaScriptEnabled = true
        binding.webViewVideoGENERAL.settings.loadWithOverviewMode = true
        binding.webViewVideoGENERAL.settings.useWideViewPort = true


        binding.webViewVideoGENERAL.webChromeClient = WebChromeClient()

    }

    private fun mostrarVideo(video: Video) {

        linkP = video.LINK.toString()
        binding.webViewVideoGENERAL.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$linkP\" " +
                "title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; " +
                "clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>", "text/html; charset=utf-8", "utf-8")

    }


    private fun actionBarpersonalizado() {

        val actionBar = supportActionBar!!
        actionBar.title = "Video"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayShowTitleEnabled(true)
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return super.onSupportNavigateUp()
    }


}