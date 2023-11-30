package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.data.network.response.DailyVerseResponse
import com.devdiegomata90.nueva_vida_app.domain.GetCategoriesUseCase
import com.devdiegomata90.nueva_vida_app.domain.GetDailyVerseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InicioClienteViewModel: ViewModel() {

    //Creacion de los MutableliveData
    var dailyVerse:MutableLiveData<DailyVerseResponse?> = MutableLiveData()

    // StateFlow para categorías
    private val _categories = MutableStateFlow<List<Categoria?>>(emptyList())
    val categories get() = _categories


    //Llamamos al caso de uso que obtiene
    val getDailyVerseUseCase = GetDailyVerseUseCase()
    val getCategoriesUseCase = GetCategoriesUseCase()

    //Funciones
    fun Oncreate(){
        viewModelScope.launch {

            val result = getDailyVerseUseCase()

            if (result != null) {
                dailyVerse.postValue(result)
            }

            //Obtener las categorias
            showCategories()
        }

    }

    fun shareText(context: Context, text: String) {

        // Crear un Intent
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        // Iniciar la actividad de compartir
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    suspend fun showCategories(){

        val result = this.getCategoriesUseCase()

        // Observar cambios en las categorías
        result.collect { _categories.value = it }

    }


}




