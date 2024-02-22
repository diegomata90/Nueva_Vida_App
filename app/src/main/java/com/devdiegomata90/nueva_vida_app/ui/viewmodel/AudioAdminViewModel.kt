package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.domain.GetAudiosUseCase
import com.devdiegomata90.nueva_vida_app.domain.DeleteAudioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AudioAdminViewModel: ViewModel() {

    //Creacion de los MutableliveData
    private val _audios = MutableStateFlow<List<Audio?>>(emptyList())
    val audios get() = _audios

    private val _message= MutableLiveData<String>()
    val message get() = _message


    //Llamamos al caso de uso que obtiene
    val getAudiosUseCase = GetAudiosUseCase()
    val deleteAudioUseCase = DeleteAudioUseCase()


    //Funciones
    fun Oncreate(){
        getAudios()
    }

    fun getAudios() {
        viewModelScope.launch {

            val result = getAudiosUseCase()

            if (result != null) {
                // Observar cambios en las categorías
                result.collect { _audios.value = it }
            }

        }
    }

    fun deleteAudio(audio: Audio) {

        viewModelScope.launch {

            val result = deleteAudioUseCase(audio)

            if(result){
                _message.value = "Audio eliminado"
            }else{
                _message.value = "Error al eliminar el audio"
            }
        }

    }

}