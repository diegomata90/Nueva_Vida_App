package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AudioRepository
import kotlinx.coroutines.flow.Flow

class GetAudiosUseCase {

    private val repository = AudioRepository()

    suspend operator fun invoke(): Flow<List<Audio?>>{
        return repository.getAllAudioFlow()
    }
}