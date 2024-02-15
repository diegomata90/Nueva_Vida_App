package com.devdiegomata90.nueva_vida_app.ui.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.domain.AddAudioUseCase
import com.devdiegomata90.nueva_vida_app.domain.UpdateAudioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AudioAgregarViewModel : ViewModel() {

    //Creacion de los MutableliveData
    private val _audios = MutableStateFlow<List<Audio?>>(emptyList())
    val audios get() = _audios

    private val _message = MutableLiveData<String>()
    val message get() = _message

    private val _successful = MutableLiveData<Boolean>()
    val successful get() = _successful

    //Dialogo logging
    private val _loading = MutableLiveData<Boolean>()
    val loading get() = _loading

    //Casos de Uso
    val addAudioUseCase = AddAudioUseCase()
    val updateAudioUseCase = UpdateAudioUseCase()

    //Inicializa la funcion Oncreate para el viewModel
    fun onCreate() {

    }


    //Funciones

    fun addAudio(audio:Audio){
        viewModelScope.launch {

            //Inicializar el dialogo
            _loading.value = true

            val result = addAudioUseCase(audio)
            _successful.value = result

            if (result) {
                _message.value = "Audio Agregado"
                //Finalizar el dialogo
                _loading.value = false
            } else {
                _message.value = "Error al agregar el audio"
                //Finalizar el dialogo
                _loading.value = false
            }

        }
    }



    //Actualiza el audio
    fun updateAudio(audio: Audio) {


        viewModelScope.launch {

            //Inicilizar el dialogo
            _loading.value = true

            val result = updateAudioUseCase(audio)
            _successful.value = result

            if (result) {
                _message.value = "Audio Modificado"
                //Finalizar el dialogo
                _loading.value = false
            } else {
                _message.value = "Error al actualizar el audio"
                //Finalizar el dialogo
                _loading.value = false
            }
        }


    }

}