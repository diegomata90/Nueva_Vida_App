package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.domain.AddAudioUseCase
import com.devdiegomata90.nueva_vida_app.ui.view.AudioA.AudioAgregarActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AudioAgregarViewModel: ViewModel(){

    //Creacion de los MutableliveData
    private val _audios = MutableStateFlow<List<Audio?>>(emptyList())
    val audios get() = _audios

    private val _message= MutableLiveData<String>()
    val message get() = _message

    private val _successful = MutableLiveData<Boolean>()
    val successful get() = _successful

    //Dialogo logging
    private val _loading = MutableLiveData<Boolean>()
    val loading get() = _loading

    //Casos de uso
    val addAudioUseCase = AddAudioUseCase()

    //Inicializa la funcion Oncreate para el viewModel
    fun Oncreate(){

    }


    //Funciones
    fun addAudio(audio: Audio, RutaArchivoUri: Uri? = null, RutaAudioUri: Uri,extensiones:Pair<String?,String?>) {

        viewModelScope.launch {

            //Inicilizar el dialogo
            _loading.value = true

            val result = addAudioUseCase(audio,RutaArchivoUri,RutaAudioUri!!,extensiones)
            _successful.value = result

            if(result){
                _message.value = "Audio Agregado"
                //Inicilizar el dialogo
                _loading.value = false
            }else{
                _message.value = "Error al agregar el audio"
                //Inicilizar el dialogo
                _loading.value = false
            }
        }
    }

    fun editAudio(audio: Audio){

    }

}