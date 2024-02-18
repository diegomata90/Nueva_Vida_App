package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.domain.SendEmailUseCase
import kotlinx.coroutines.launch

class RestaurarPassViewModel : ViewModel() {
    //Livedata
    private val _message= MutableLiveData<String?>()
    val message get() = _message

    //Dialogo logging
    private val _loading = MutableLiveData<Boolean>()
    val loading get() = _loading

    //Casos de uso
    val sendEmailUseCase = SendEmailUseCase()



    //Funciones
    fun sendEmail(email:String){

        //Iniciar la coorutina
        viewModelScope.launch {

            //Inicializar loading
            _loading.value = true

            val result = sendEmailUseCase(email)

            if (result) {
                _message.value = "Email enviado"
                //Finalizar el dialogo
                _loading.value = false
            } else {
                _message.value = "Error al enviar el email"
                //Finalizar el dialogo
                _loading.value = false
            }

        }


    }

}