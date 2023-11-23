package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.BookRepository
import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse

class GetBooksUseCase {
    private val repository = BookRepository()

    suspend operator fun invoke():List<BooksResponse>? = repository.getAllBooks()

}