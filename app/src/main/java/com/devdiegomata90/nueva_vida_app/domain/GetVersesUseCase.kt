package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.BookProvider
import com.devdiegomata90.nueva_vida_app.data.model.BookAndChapter
import com.devdiegomata90.nueva_vida_app.data.repository.BookRepository
import com.devdiegomata90.nueva_vida_app.data.repository.VerseRepository
import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse

class GetVersesUseCase {

    private val repository = VerseRepository()

    suspend operator fun invoke(bookSelected:String, chapterSelected: String): Pair<BookAndChapter, List<VersesResponse>>? {

        //Recupera los libros de repositorio
        val books = BookProvider.books

        //obtiene el id del libro
        val IDBOOK = books.filter { it.name == bookSelected }.map{it.version_book}.first().toString()

        //Forma el URL
        val Url = "books/$IDBOOK/verses/$chapterSelected"

        val verses = repository.getAllVerses(Url)

        if(!verses.isNullOrEmpty()){
            //ordena la lista
            val orderVerses = verses.sortedBy{it.id}

            //devuelve el libro y capitulo
            val bookAndChapter = BookAndChapter(bookSelected, chapterSelected)

            //devuel 2 valores
            return Pair(bookAndChapter, orderVerses)
        }

        return null
    }

}