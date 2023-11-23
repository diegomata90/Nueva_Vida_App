package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.core.RetrofitHelper
import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class VerseService {

    private val retrofit = RetrofitHelper.getretrofit()

    suspend fun getVeses(url:String): List<VersesResponse> {

        // Llama a la función getAllBooks de la API en un hilo secundario despues devuelve la lista de libros
        return withContext(Dispatchers.IO) {
            val response: Response<List<VersesResponse>> =
                retrofit.create(VerseApiClient::class.java).getVerses(url)

            // Devuelve la lista de libros
            response.body() ?: emptyList()

        }


    }
}