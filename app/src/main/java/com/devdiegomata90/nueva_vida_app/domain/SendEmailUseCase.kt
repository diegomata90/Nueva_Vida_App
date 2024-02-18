package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class SendEmailUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(email: String): Boolean = repository.resetPass(email)



}