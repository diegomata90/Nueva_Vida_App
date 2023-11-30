package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.Book
import com.devdiegomata90.nueva_vida_app.data.repository.BookRepository

class GetBooksUseCase {
    private val repository = BookRepository()

    suspend operator fun invoke():List<Book>? = repository.getAllBooks()

}