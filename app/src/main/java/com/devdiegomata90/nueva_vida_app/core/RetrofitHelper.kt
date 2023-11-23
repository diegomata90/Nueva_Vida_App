package com.devdiegomata90.nueva_vida_app.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getretrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}