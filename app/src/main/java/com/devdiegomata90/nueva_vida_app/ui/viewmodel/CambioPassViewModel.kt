package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.devdiegomata90.nueva_vida_app.domain.ChangePassUseCase
import kotlinx.coroutines.launch

class CambioPassViewModel : ViewModel() {

    //Live Data
    private val _message: MutableLiveData<String> = MutableLiveData()
    val message get() = _message

    private val _successful: MutableLiveData<Boolean> = MutableLiveData()
    val successful get() = _successful

    //Dialogo logging
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    //Casos de uso
    var changePassUseCase = ChangePassUseCase()


    //Inicializa la funcion Oncreate para el viewModel
    fun onCreate() {

    }

    //Funciones

    fun changePass(newPass: String) {

        viewModelScope.launch {

            //
            _loading.value = true
            successful.value = changePassUseCase(newPass)

            if (successful.value == true) {
                _message.value = "Contraseña cambiada"
            }else {
                _message.value = "Error al cambiar la contraseña"
            }
        }

    }

}