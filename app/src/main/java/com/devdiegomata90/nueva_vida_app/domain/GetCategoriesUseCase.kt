package com.devdiegomata90.nueva_vida_app.domain


import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase () {

    private val repository :CategoryRepository = CategoryRepository()
    suspend operator fun invoke(): Flow<List<Categoria?>> {
        return repository.getAllCategoriesFlow()
    }

}