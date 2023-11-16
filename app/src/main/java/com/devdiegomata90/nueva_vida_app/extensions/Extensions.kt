package com.devdiegomata90.nueva_vida_app.extensions

import com.devdiegomata90.nueva_vida_app.model.Book
import com.devdiegomata90.nueva_vida_app.model.Verse
import com.devdiegomata90.nueva_vida_app.network.BooksResponse
import com.devdiegomata90.nueva_vida_app.network.VersesResponse

//Transforma las listados de libros a objetos Book para utilizar la extension function
//Tambien transforma las listadas de versiculos a verse
object Extensions {

    fun BooksResponse.toBook(): Book {
        return Book(
            codigo = this.codigo,
            version_book = this.version_book,
            name = this.name,
            order = this.order,
            testament = this.testament,
            capitulos = this.capitulos
        )
    }

    fun VersesResponse.toVerse(): Verse {
        return Verse(
            codigo = this.codigo,
            capituloId = this.capituloId,
            cleanText = this.cleanText,
            id = this.id,
            capitulo =this.capitulo,
            htmlText = this.htmlText
        )
    }
}