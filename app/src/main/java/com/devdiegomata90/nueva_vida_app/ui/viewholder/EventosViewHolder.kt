package com.devdiegomata90.nueva_vida_app.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class EventosViewHolder(private val view: View, onItemClickListener: onItemClickListener):RecyclerView.ViewHolder(view) {

    //Pintamos las vista
    private var tituloEvento:TextView = view.findViewById(R.id.TituloEvento)
    private var fechaEvento:TextView = view.findViewById(R.id.FechaEvento)
    private val lugarEvento:TextView = view.findViewById(R.id.LugarEvento)
    private val horaEvento:TextView = view.findViewById(R.id.HoraEvento)
    private val imageEvento:ImageView =view.findViewById(R.id.imageEvento)
    lateinit var EventoArray: Evento


    //METODO PARA LA ACCION DEL ADMINISTRADOR
    interface onItemClickListener {

        fun onClick(evento: Evento)
        fun onLongClick(evento: Evento)

    }

    init {
            view.setOnClickListener {
                onItemClickListener.onClick(EventoArray)
            }
            view.setOnLongClickListener {
                onItemClickListener.onLongClick(EventoArray)
                return@setOnLongClickListener true
        }
    }


    //Metodo para setear los valores de la vista
    fun bind(evento: Evento){

        //Asignamos los datos
        EventoArray = evento

        //Asigna texto de basedatos al textView
        tituloEvento.text = evento.titulo
        fechaEvento.text = evento.fecha
        lugarEvento.text = evento.lugar
        val imagen = evento.imagen


        // Dar formato a la fecha "05 de octubre del 2023"
        val fechaSinFormato = evento.fecha.toString()
        val fechaFormateada = formatFecha(fechaSinFormato)
        fechaEvento.text = fechaFormateada

        // Dar formato a la hora de "10:10 a.m"
        val horasinFormato = evento.hora.toString()
        val horaFormateada = formatHora(horasinFormato)
        horaEvento.text = horaFormateada


        ///Capturar la imagen con libreria Picaso
        ///Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).placeholder(R.drawable.image_ico).into(imageEvento)
        } catch (e: Exception) {
            //Si la imagen no fue traida exitosamente
            Picasso.get().load(R.drawable.image_ico).into(imageEvento)
        }

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disenada para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(itemView.context,
            null,
            tituloEvento,
            fechaEvento,
            lugarEvento,
            horaEvento
        )


    }

    private fun formatFecha(fechaSinFormato: String): String {
        // Dividir la fecha en sus componentes
        val partes = fechaSinFormato.split("-")
        if (partes.size != 3) {
            // Manejar un formato de fecha incorrecto (debería ser dd-mm-yyyy)
            return fechaSinFormato
        }

        val dia = partes[0]
        val mesNumerico = partes[1]
        val anio = partes[2]

        // Convertir el componente del mes a su representación textual en español
        val meses = arrayOf(
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        val mesTexto = if (mesNumerico.toIntOrNull() in 1..12) {
            meses[mesNumerico.toInt() - 1]
        } else {
            // Manejar un mes inválido
            return fechaSinFormato
        }

        // Formatear la fecha en el nuevo formato
        return "$dia de $mesTexto del $anio"

}

    private fun formatHora(hora:String):String {

        // Parsear la hora en formato "HH:mm" a una instancia de Date
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return try {
            val parsedTime = inputFormat.parse(hora)
            val formattedTime = outputFormat.format(parsedTime)

            // retorna la hora formateada
            formattedTime.toString()

        } catch (e: ParseException) {
            // Manejar errores de formato de hora aquí
            hora
        }

    }

}








