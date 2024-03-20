package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.devdiegomata90.nueva_vida_app.domain.GetVideosUseCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VideoViewModel : ViewModel() {
    //Livedata
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos get() = _videos

    //Casos de uso
    private val getVideosUseCase = GetVideosUseCase()

    //Funciones
    fun onCreate() {
        getVideos()
    }

    private fun getVideos() {
        viewModelScope.launch {
            val result = getVideosUseCase()

            if (result != null) {
                result.collect {
                    _videos.value = it
                    Log.d("FVIDEOS2", "VIDEOS: ${it}")
                }
            }
        }
    }
}