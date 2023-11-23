package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.network.VerseService
import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse

class VerseRepository {

    private val api = VerseService()

    suspend fun getAllVerses(url: String): List<VersesResponse> {

        val response = api.getVeses(url)

        return response
    }
}