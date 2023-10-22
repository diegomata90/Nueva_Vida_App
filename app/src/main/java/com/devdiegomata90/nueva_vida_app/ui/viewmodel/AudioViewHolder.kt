package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.squareup.picasso.Picasso


class AudioViewHolder(private val view: View) : RecyclerView.ViewHolder(view){

    //Pintamos las vista
    private val audioTitulo: TextView = view.findViewById(R.id.TituloAudio)
    private val descripcionAudio: TextView = view.findViewById(R.id.DescripcionAudio)
    private val fechaAudio: TextView = view.findViewById(R.id.FechaAudio)
    private val imageAudio: ImageView =view.findViewById(R.id.imageAudio)


    //Metodo para setear los valores de la vista
    fun bind(audio: Audio){

        //Asigna texto de basedatos al textView
        audioTitulo.text = audio.titulo
        descripcionAudio.text = audio.descripcion
        fechaAudio.text = audio.fecha
        val imagen = audio.imagen


        ///Capturar la imagen con libreria Picaso
        ///Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(imageAudio)
        } catch (e: Exception) {
            //Si la imagen no fue traida exitosamente
            Picasso.get().load(R.drawable.categoria).into(imageAudio)
        }

    }

}