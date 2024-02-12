package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AudioRepository


class UpdateAudioUseCase {

    private val repository = AudioRepository()

    suspend operator fun invoke(audio: Audio): Boolean = repository.updateAudio(audio)
}