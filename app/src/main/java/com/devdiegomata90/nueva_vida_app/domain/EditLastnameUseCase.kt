package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class EditLastnameUseCase {
    private val repository = UserRepository()

    suspend operator fun invoke(lastname: String): Boolean = repository.updateLastname(lastname)
}