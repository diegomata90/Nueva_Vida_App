package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devdiegomata90.nueva_vida_app.data.model.Categoria

class OtrasCategoriaViewModel: ViewModel() {

    //Creacion de los MutableliveData
    private val _categories = MutableLiveData<List<Categoria?>>(emptyList())
    val categories get() = _categories


    //Metodo Funciones
    fun onCreate(){

    }

    //Funciones


}