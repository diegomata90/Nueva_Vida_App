package com.devdiegomata90.nueva_vida_app.domain

import android.net.Uri
import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class UpdateImageUseCase {

    private val repository = UserRepository()
    suspend operator fun invoke(image: String, extension: String): Boolean = repository.updateImage(image,extension)
}