package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.data.model.Book
import retrofit2.Response
import retrofit2.http.GET

interface BookApiClient {
    @GET("books/")
    suspend fun getAllBooks(): Response<List<Book>>
}