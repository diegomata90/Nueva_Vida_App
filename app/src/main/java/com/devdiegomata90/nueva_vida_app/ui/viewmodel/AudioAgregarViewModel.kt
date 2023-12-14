package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.domain.AddAudioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AudioAgregarViewModel: ViewModel(){

    //Creacion de los MutableliveData
    private val _audios = MutableStateFlow<List<Audio?>>(emptyList())
    val audios get() = _audios

    private val _message= MutableLiveData<String>()
    val message get() = _message

    //Casos de uso
    val addAudioUseCase = AddAudioUseCase()

    //Inicializa la funcion Oncreate para el viewModel
    fun Oncreate(){

    }


    //Funciones
    fun addAudio(audio: Audio){

        viewModelScope.launch {

            val result = addAudioUseCase(audio)

            if(result){
                _message.value = "Audio Agregado"
            }else{
                _message.value = "Error al agregar el audio"
            }
        }
    }

}