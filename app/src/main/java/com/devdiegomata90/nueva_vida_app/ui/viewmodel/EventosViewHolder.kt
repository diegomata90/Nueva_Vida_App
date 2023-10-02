package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.squareup.picasso.Picasso


class EventosViewHolder(view:View):RecyclerView.ViewHolder(view) {

    //Pintamos las vista
    private var tituloEvento:TextView = view.findViewById(R.id.TituloEvento)
    private var fechaEvento:TextView = view.findViewById(R.id.FechaEvento)
    private val lugarEvento:TextView = view.findViewById(R.id.LugarEvento)
    private val horaEvento:TextView = view.findViewById(R.id.HoraEvento)
    private val imageEvento:ImageView =view.findViewById(R.id.imageEvento)

    fun render(eventos: Evento){

        //Asigna texto de basedatos al textView
        tituloEvento.text = eventos.titulo
        fechaEvento.text = eventos.fecha
        lugarEvento.text = eventos.lugar
        horaEvento.text = eventos.hora
        var imagen = eventos.imagen
        

        ///Capturar la imagen con libreria Picaso

        ///Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(imageEvento)
        } catch (e: Exception) {
            //Si la imagen no fue traida exitosamente
            Picasso.get().load(R.drawable.categoria).into(imageEvento)
        }
        
        
    }


}