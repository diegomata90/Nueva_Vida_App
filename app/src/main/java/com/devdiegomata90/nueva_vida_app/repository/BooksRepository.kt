package com.devdiegomata90.nueva_vida_app.repository

import com.devdiegomata90.nueva_vida_app.model.Book

interface BooksRepository {
    suspend fun getBooks(url: String): List<Book>

}