package com.devdiegomata90.nueva_vida_app.data

import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse

class BookProvider {
    //para almacenar en memoria los libros
    companion object {
        var books :List<BooksResponse> = emptyList()
    }
}