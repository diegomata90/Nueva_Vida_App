package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.network.VerseService
import com.devdiegomata90.nueva_vida_app.data.model.Verse

class VerseRepository {

    private val api = VerseService()

    suspend fun getAllVerses(url: String): List<Verse> {

        val response = api.getVerses(url)

        return response
    }
}