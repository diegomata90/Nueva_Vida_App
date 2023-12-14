package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AddAudioRepository

class AddAudioUseCase {

    private val repository = AddAudioRepository()

    suspend operator fun invoke(audio: Audio):Boolean= repository.addAudio(audio)

}