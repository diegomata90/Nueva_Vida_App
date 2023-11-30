package com.devdiegomata90.nueva_vida_app.data.network

import com.devdiegomata90.nueva_vida_app.core.RetrofitHelperApp
import com.devdiegomata90.nueva_vida_app.data.network.response.DailyVerseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DailyVerseService {

    private val retrofit = RetrofitHelperApp.getRetrofit()

    suspend fun getVerse(url: String): DailyVerseResponse {

        return withContext(Dispatchers.IO){
            val response : Response<DailyVerseResponse> =
                retrofit.create(DailyVerseApiClient::class.java).getDailyVerse(url)

            //Devuelve el Versiculo
            response.body() ?: DailyVerseResponse()
        }

    }
}