package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.devdiegomata90.nueva_vida_app.domain.ChangePassUseCase

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
    val changePassUseCase: ChangePassUseCase()


    //Inicializa la funcion Oncreate para el viewModel
    fun Oncreate() {

    }

    //Funciones

    fun changePass(oldPass: String, newPass: String, confirmPass: String) {

    }

}