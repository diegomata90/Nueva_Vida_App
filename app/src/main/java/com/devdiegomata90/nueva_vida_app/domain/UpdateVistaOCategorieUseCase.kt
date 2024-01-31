package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.CategoryDetailRepository


class UpdateVistaOCategorieUseCase {

    private val repository = CategoryDetailRepository()

    suspend operator fun invoke(id:String, cat:String): Boolean = repository.updateVistasOCategorie(id,cat)
}