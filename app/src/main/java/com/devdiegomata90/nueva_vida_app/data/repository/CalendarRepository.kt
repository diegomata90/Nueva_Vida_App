package com.devdiegomata90.nueva_vida_app.data.repository


import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import java.util.Calendar


class CalendarRepository {

    suspend fun addEvent(context: Context, evento: Evento ) {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("title", evento.titulo)

        // Convertir la fecha y hora a milisegundos
        val calendario = Calendar.getInstance()
        val fechaSplit = evento.fecha!!.split("-")
        val horaSplit = evento.hora!!.split(":")

        calendario.set(Calendar.YEAR, fechaSplit[2].toInt())
        calendario.set(Calendar.MONTH, fechaSplit[1].toInt() - 1)
        calendario.set(Calendar.DAY_OF_MONTH, fechaSplit[0].toInt())
        calendario.set(Calendar.HOUR_OF_DAY, horaSplit[0].toInt())
        calendario.set(Calendar.MINUTE, horaSplit[1].toInt())
        val beginTime = calendario.timeInMillis

        intent.putExtra("beginTime", beginTime)
        intent.putExtra("allDay", false)
        intent.putExtra("description", evento.descripcion)

        context.startActivity(intent)

    }



}