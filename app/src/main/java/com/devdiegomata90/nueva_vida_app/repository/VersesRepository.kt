package com.devdiegomata90.nueva_vida_app.repository

import com.devdiegomata90.nueva_vida_app.model.Verse

interface VersesRepository {
    suspend fun getVerses(url: String): List<Verse>
}