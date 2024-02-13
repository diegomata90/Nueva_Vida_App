package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AudioRepository


private val repository = AudioRepository()

class DeleteAudioUseCase {

   suspend operator fun invoke(audio: Audio): Boolean {return repository.deleteAudio(audio)}
}