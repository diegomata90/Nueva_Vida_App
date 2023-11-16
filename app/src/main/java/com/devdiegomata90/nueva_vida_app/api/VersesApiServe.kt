package com.devdiegomata90.nueva_vida_app.api

import com.devdiegomata90.nueva_vida_app.network.VersesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface VersesApiServe {
    @GET()
    suspend fun getVerses(@Url url:String): Response<List<VersesResponse>>
}