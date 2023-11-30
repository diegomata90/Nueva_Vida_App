package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.network.DailyVerseService
import com.devdiegomata90.nueva_vida_app.data.model.DailyVerseResponse

class DailyVerseRepository {

    private val api = DailyVerseService()

    suspend fun getDailyVerse(url: String): DailyVerseResponse {
        val response = api.getVerse(url)
        return response
    }
}