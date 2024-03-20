package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.devdiegomata90.nueva_vida_app.data.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class GetVideosUseCase {

    private val repository = VideoRepository()

    suspend operator fun invoke(): Flow<List<Video>> = repository.getVideos()
}