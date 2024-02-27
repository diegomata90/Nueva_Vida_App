package com.devdiegomata90.nueva_vida_app.domain

import android.graphics.Bitmap
import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class UpdateImageCamaraUseCase {
    private val repository = UserRepository()

    suspend operator fun invoke(bitmap: Bitmap, extension: String): Boolean = repository.updateImageCamara(bitmap,extension)
}