package com.devdiegomata90.nueva_vida_app.ui.viewmodel


import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.domain.EditLastnameUseCase
import com.devdiegomata90.nueva_vida_app.domain.EditNameUseCase
import com.devdiegomata90.nueva_vida_app.domain.GetUserUseCase
import com.devdiegomata90.nueva_vida_app.domain.UpdateImageCamaraUseCase
import com.devdiegomata90.nueva_vida_app.domain.UpdateImageUseCase
import kotlinx.coroutines.launch
import java.io.File

class PerfilAdminViewModel: ViewModel() {

    //Livedata
    private val _user = MutableLiveData<User?>()
    val user get() = _user

    private val _message = MutableLiveData<String>()
    val message get() = _message



    //Casos de uso
    val getUserUseCase = GetUserUseCase()
    val editNameUseCase = EditNameUseCase()
    val editLastnameUseCase = EditLastnameUseCase()
    val updateImageUseCase = UpdateImageUseCase()
    val updateImageCamaraUseCase = UpdateImageCamaraUseCase()


    //Funciones
    fun onCreate(){
        getUser()
    }

    private fun getUser(){

        viewModelScope.launch {
            val result = getUserUseCase()

            _user.value = result
        }

    }

    fun editName(name: String){
        viewModelScope.launch {
            val result = editNameUseCase(name)
            if (result) {
                _message.value = "Nombre editado"
                onCreate()
            }
        }
    }

    fun editLastname(lastname: String){
        viewModelScope.launch {
            val result = editLastnameUseCase(lastname)
            if (result) {
                _message.value = "Apellido editado"
                onCreate()
            }
        }
    }

    fun editImage(url: String, extension: String) {
        viewModelScope.launch {
            val result = updateImageUseCase(url,extension)
            if (result) {
                _message.value = "Imagen editada"
                onCreate()
            }else{
                _message.value = "Error al editar la imagen"
            }

        }

    }

    fun editImageCamara(bitmap: Bitmap, extension: String) {
        viewModelScope.launch {
            val result = updateImageCamaraUseCase(bitmap,extension)
            if (result){
                _message.value = "Imagen editada"
                onCreate()
            }else{
                _message.value = "Error al editar la imagen"
            }
        }
    }
}
