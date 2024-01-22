package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.devdiegomata90.nueva_vida_app.domain.GetCategorieDetailUseCase
import kotlinx.coroutines.launch

class OtrasCategoriasViewModel: ViewModel() {

    //Creacion de los MutableliveData
    private val _oCategories = MutableLiveData<List<CategoriaDetalle?>>(emptyList())
    val oCategories get() = _oCategories

    //Casos de uso
    val getCategorieDetailUseCase = GetCategorieDetailUseCase()


    //Oncreate
    fun onCreate(){

    }

    //Function
    fun showCategorieDetail(categoryName: String){


        viewModelScope.launch {

            val result = getCategorieDetailUseCase(categoryName)

            // Observar cambios en las categorías
            result.collect({ _oCategories.value = it })

        }
    }



}
