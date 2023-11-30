package com.devdiegomata90.nueva_vida_app.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelperApp {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://app-nueva-vida-default-rtdb.firebaseio.com/DAILYVERSE/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}