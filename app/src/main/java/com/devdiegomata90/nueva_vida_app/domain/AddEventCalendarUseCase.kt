package com.devdiegomata90.nueva_vida_app.domain

import android.content.Context
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.data.repository.CalendarRepository

class AddEventCalendarUseCase {

    //Repository
    private val repository = CalendarRepository()

    suspend operator fun invoke(context: Context, event: Evento) = repository.addEvent(context,event)
}