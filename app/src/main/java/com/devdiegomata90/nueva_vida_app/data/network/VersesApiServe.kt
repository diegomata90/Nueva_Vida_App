package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface VersesApiServe {
    @GET()
    suspend fun getVerses(@Url url:String): Response<List<VersesResponse>>
}