package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.core.RetrofitHelperBiblia
import com.devdiegomata90.nueva_vida_app.data.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BookService {

    private val retrofit = RetrofitHelperBiblia.getretrofit()

    suspend fun getBooks(): List<Book> {

        // Llama a la función getAllBooks de la API en un hilo secundario despues devuelve la lista de libros
        return withContext(Dispatchers.IO) {
            val response: Response<List<Book>> =
                retrofit.create(BookApiClient::class.java).getAllBooks()

            // Devuelve la lista de libros
            response.body() ?: emptyList()

        }


    }
}




