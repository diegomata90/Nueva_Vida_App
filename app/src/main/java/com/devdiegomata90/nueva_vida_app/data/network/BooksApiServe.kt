package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface BooksApiServe {
    @GET()
    suspend fun getBooks(@Url url:String): Response<List<BooksResponse>>
}