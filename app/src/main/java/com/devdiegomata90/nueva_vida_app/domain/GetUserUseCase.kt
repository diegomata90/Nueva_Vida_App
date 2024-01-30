package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class GetUserUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(): User?= repository.getUser()
}