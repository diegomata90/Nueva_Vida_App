package com.devdiegomata90.nueva_vida_app.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.squareup.picasso.Picasso

class OtrasCategoriasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //Conectamos la vista con el XML
    private val titulo: TextView = itemView.findViewById(R.id.CategoriasDetalleTitulo)
    private val imagen: AppCompatImageView = itemView.findViewById(R.id.imageCategoriaDetalle)
    private val autor: TextView = itemView.findViewById(R.id.AutorCategoriaDetalle)
    private val description: TextView = itemView.findViewById(R.id.DescripCategoriaDetalle)
    private val vistas: TextView = itemView.findViewById(R.id.Vistas)

    fun bind(categoriaDetalle: CategoriaDetalle, onClickListener: (CategoriaDetalle) -> Unit) {

        //Asignamos los valores al XML
        titulo.text = categoriaDetalle.titulo
        autor.text = categoriaDetalle.autor
        description.text = categoriaDetalle.descripcion
        vistas.text = categoriaDetalle.vistas.toString()


        //Setear o asignar la imagen al XML
        try {
            //Asignar la imagen en la image viewer
            Picasso.get().load(categoriaDetalle.imagen).into(imagen)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.image_ico).into(imagen)
        }

        //Asignar el evento al item para el evento de click
        itemView.setOnClickListener { onClickListener(categoriaDetalle) }

        //Cambiar el tipo de letras
        //Asigna Tipo Letra de Ubuntu a los texView (funcion Disenada para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(itemView.context,
            null,
            titulo,
            autor,
            description
        )

    }


}