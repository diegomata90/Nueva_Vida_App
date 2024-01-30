package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.domain.GetUserUseCase
import kotlinx.coroutines.launch

class InicioAdminViewModel : ViewModel() {

    // Livedata
    private val _user = MutableLiveData<User?>()
    val user get() = _user

    //Casos de uso
    val getUserUseCase = GetUserUseCase()


    // Funcion onCreate
    fun Oncreate() {

        //Consulta obtener los datos del usuario
      setUser()

    }

    //funciones
    fun setUser() {

        viewModelScope.launch {
            val result = getUserUseCase()

            _user.value = result
        }

    }


}