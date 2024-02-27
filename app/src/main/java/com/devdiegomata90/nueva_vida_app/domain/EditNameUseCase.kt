package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class EditNameUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(name: String): Boolean = repository.updateName(name)
}