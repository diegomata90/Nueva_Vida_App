package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.squareup.picasso.Picasso

class AudioListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


    val audioTitle: TextView = itemView.findViewById(R.id.audio_title)
    val audioDescription: TextView = itemView.findViewById(R.id.DescripcionAudio)
    val audioPreview: ImageView = itemView.findViewById(R.id.audio_preview)


    fun bind(audio: Audio, onClickListener: (Audio) -> Unit) {

        audioTitle.text = audio.titulo
        audioDescription.text = audio.descripcion

        try {
            //Asignar la imagen en la image viewer
            Picasso.get().load(audio.imagen).into(audioPreview)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.album).into(audioPreview)
        }

        itemView.setOnClickListener { onClickListener(audio) }

    }


}