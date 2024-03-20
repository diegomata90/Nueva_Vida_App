package com.devdiegomata90.nueva_vida_app.ui.viewholder


import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.squareup.picasso.Picasso

class VideoViewHolder (val view: View): RecyclerView.ViewHolder(view) {

    private val videoTitulo: TextView = view.findViewById(R.id.tvTituloVideo)
    private val videoDescrip: TextView = view.findViewById(R.id.tvDesVideo)
    private val imagenVideo: AppCompatImageView = view.findViewById(R.id.imageVideo)

    fun bind(video: Video, onClickListener: (Video) -> Unit) {
        videoTitulo.text = video.TITULO
        videoDescrip.text = video.DESCRIPCION

        val image = video.IMAGE

        //Pintamos la imagen
        try {
            Picasso.get().load(image).into(imagenVideo)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.image_ico).into(imagenVideo)
        }

        //Dar evento al item
        itemView.setOnClickListener { onClickListener(video) }


    }

}