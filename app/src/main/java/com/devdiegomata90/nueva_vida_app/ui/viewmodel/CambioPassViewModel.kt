package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.User

import com.devdiegomata90.nueva_vida_app.domain.ChangePassUseCase
import com.devdiegomata90.nueva_vida_app.domain.GetUserUseCase
import kotlinx.coroutines.launch

class CambioPassViewModel : ViewModel() {

    //Live Data
    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user get() = _user

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message get() = _message

    private val _successful: MutableLiveData<Boolean> = MutableLiveData()
    val successful get() = _successful

    //Dialogo logging
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    //Casos de uso
    var changePassUseCase = ChangePassUseCase()
    var getUserUseCase = GetUserUseCase()


    //Inicializa la funcion Oncreate para el viewModel
    fun onCreate() {
        getUser()
    }

    //Funciones
    private fun getUser() {
        viewModelScope.launch {
            val result = getUserUseCase()
            if (result != null) {
                _user.value = result
            }
        }
    }


    fun changePass(email: String, currentPass: String, newPass: String) {

        viewModelScope.launch {

            //Inicilizar el dialogo
            _loading.value = true
            successful.value = changePassUseCase(email,currentPass,newPass)

            if (successful.value == true) {
                _loading.value = false
                _message.value = "Contraseña cambiada"
            }else {
                _loading.value = false
                _message.value = "Error al cambiar la contraseña"
            }
        }

    }

}