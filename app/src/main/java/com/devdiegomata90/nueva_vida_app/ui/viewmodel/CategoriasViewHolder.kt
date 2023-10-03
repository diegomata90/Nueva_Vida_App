package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Categoria


class CategoriasViewHolder(view: View):RecyclerView.ViewHolder(view) {

    //pintamos las vista
    private val tvCategoriaName: TextView = view.findViewById(R.id.tvCategoriaName)
    private val imgCategoria: AppCompatImageView = view.findViewById(R.id.imgCategoria)

    fun render(categoria: Categoria){
        tvCategoriaName.text = categoria.nombre

        //mostrar la imagen
        //imgCategoria.
    }
}