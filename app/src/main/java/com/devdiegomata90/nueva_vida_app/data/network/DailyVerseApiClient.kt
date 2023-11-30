package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.data.network.response.DailyVerseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface DailyVerseApiClient {

    @GET()
    suspend fun getDailyVerse(@Url url: String): Response<DailyVerseResponse>
}