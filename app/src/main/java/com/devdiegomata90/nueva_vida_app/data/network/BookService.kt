package com.devdiegomata90.nueva_vida_app.data.network


import com.devdiegomata90.nueva_vida_app.core.RetrofitHelper
import com.devdiegomata90.nueva_vida_app.data.model.Book
import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BookService {

    private val retrofit = RetrofitHelper.getretrofit()

    suspend fun getBooks(): List<BooksResponse> {

        // Llama a la función getAllBooks de la API en un hilo secundario despues devuelve la lista de libros
        return withContext(Dispatchers.IO) {
            val response: Response<List<BooksResponse>> =
                retrofit.create(BookApiClient::class.java).getAllBooks()

            // Devuelve la lista de libros
            response.body() ?: emptyList()

        }


    }
}




