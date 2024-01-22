package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.devdiegomata90.nueva_vida_app.data.repository.CategoryDetailRepository
import kotlinx.coroutines.flow.Flow

class GetCategorieDetailUseCase {

    private val repository = CategoryDetailRepository()

    suspend operator fun invoke(cat:String): Flow<List<CategoriaDetalle?>> {

        return repository.getAllCategoriesDetailFlow(cat)
    }
}