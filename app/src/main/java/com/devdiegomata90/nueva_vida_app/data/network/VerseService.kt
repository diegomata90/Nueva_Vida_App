package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.core.RetrofitHelperBiblia
import com.devdiegomata90.nueva_vida_app.data.model.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class VerseService {

    private val retrofit = RetrofitHelperBiblia.getretrofit()

    suspend fun getVerses(url:String): List<Verse> {

        // Llama a la función getAllBooks de la API en un hilo secundario despues devuelve la lista de libros
        return withContext(Dispatchers.IO) {
            val response: Response<List<Verse>> =
                retrofit.create(VerseApiClient::class.java).getVerses(url)

            // Devuelve la lista de libros
            response.body() ?: emptyList()

        }


    }
}