package com.devdiegomata90.nueva_vida_app.domain

import android.content.ContentResolver
import android.net.Uri
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AudioRepository


class AddAudioUseCase {

    private val repository = AudioRepository()

    suspend operator fun invoke(audio: Audio):Boolean{

       return repository.addAudio(audio)

    }

}