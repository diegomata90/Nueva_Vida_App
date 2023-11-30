package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.BookProvider


class GetChaptersUseCase {

    //Se obtiene la lista del provider que las lista guarda en memoria
    operator fun invoke(bookSelected:String):List<String>? {

        val books = BookProvider.books

        var totalChapter: MutableList<String> = mutableListOf()

        //Filtra de la lista los capitulos del libro seleccionado y almacena los  capitulos del libro
        val result = books.filter{ it.name == bookSelected }
            .flatMap { it.capitulos  }
            .mapTo(totalChapter) { it.n_chapter.toString() }

        if(!result.isNullOrEmpty()){
            return result
        }
        return null
    }
}