package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.domain.GetAudiosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AudioAdminViewModel: ViewModel() {

    //Creacion de los MutableliveData
    private val _audios = MutableStateFlow<List<Audio?>>(emptyList())
    val audios get() = _audios


    //Llamamos al caso de uso que obtiene
    val getAudiosUseCase = GetAudiosUseCase()


    //Funciones
    fun Oncreate(){
        viewModelScope.launch {

            val result = getAudiosUseCase()

            if (result != null) {
                // Observar cambios en las categorías
                result.collect { _audios.value = it }
            }

        }
    }

}