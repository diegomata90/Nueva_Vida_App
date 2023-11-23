package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.BookProvider
import com.devdiegomata90.nueva_vida_app.data.model.Book
import com.devdiegomata90.nueva_vida_app.data.network.BookService
import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse

class BookRepository {

    private val api = BookService()

    suspend fun getAllBooks(): List<BooksResponse> {
        val response = api.getBooks()

        // Almacena la lista en memoria de el provider
        BookProvider.books = response

        return response
    }
}