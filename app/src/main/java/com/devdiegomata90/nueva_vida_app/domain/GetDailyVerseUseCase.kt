package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.network.response.DailyVerseResponse
import com.devdiegomata90.nueva_vida_app.data.repository.DailyVerseRepository
import java.util.*

class GetDailyVerseUseCase {

    private val repository = DailyVerseRepository()

    suspend operator fun invoke(): DailyVerseResponse? {
        //Obtener la fecha de hoy
        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val month = calendar.get(Calendar.MONTH)+1


        //Forma el URL
        val Url = "$day-$month.json"

        val dailyVerse = repository.getDailyVerse(Url)

        if(dailyVerse != null){
            //devuelve
            return dailyVerse
        }

        return null
    }
}