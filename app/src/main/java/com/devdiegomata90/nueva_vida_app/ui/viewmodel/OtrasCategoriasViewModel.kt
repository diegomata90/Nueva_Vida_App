package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.devdiegomata90.nueva_vida_app.domain.GetCategorieDetailUseCase
import com.devdiegomata90.nueva_vida_app.domain.UpdateVistaOCategorieUseCase
import kotlinx.coroutines.launch

class OtrasCategoriasViewModel: ViewModel() {

    //Creacion de los MutableliveData
    private val _oCategories = MutableLiveData<List<CategoriaDetalle?>>(emptyList())
    val oCategories get() = _oCategories

    //Casos de uso
    val getCategorieDetailUseCase = GetCategorieDetailUseCase()
    val updateVistaOCategorieUseCase = UpdateVistaOCategorieUseCase()


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
    fun updateVistaOCategorie(id: String, categoryName: String){

        viewModelScope.launch {
            val result = updateVistaOCategorieUseCase("$id", "$categoryName")

            if( result){
                Log.d("VISTAOCATEGORIAS", "SE ACTUALIZO CORRECTAMENTE")
            }else {
                Log.d("VISTAOCATEGORIAS", "ERROR AL ACTUALIZAR LAS VISTAS")
            }
        }
    }



}
