package com.devdiegomata90.nueva_vida_app.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ListaAdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    //Pintamos las vista
    private val nombreAdmin: TextView = itemView.findViewById(R.id.nombre_Admin)
    private val apellidoAdmin: TextView = itemView.findViewById(R.id.apellido_Admin)
    private val correoAdmin: TextView = itemView.findViewById(R.id.correo_Admin)
    private val imageAdmin: CircleImageView = itemView.findViewById(R.id.PerfilADMIN)

    fun bind(user: User, onClickListener: (User) -> Unit) {

        nombreAdmin.text = user.NOMBRES
        apellidoAdmin.text = user.APELLIDOS
        correoAdmin.text = user.CORREO

        //Asignar la imagen en la image viewer
        try {
            //Asignar la imagen en la image viewer

            //SI EXISTE LA IMAGEN EN LA BD
            Picasso.get().load(user.IMAGE).placeholder(R.drawable.perfil_item).into(imageAdmin)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.perfil_item).into(imageAdmin)
        }

        itemView.setOnClickListener { onClickListener(user) }

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disenada para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(itemView.context,
            null,
            nombreAdmin,
            apellidoAdmin,
            correoAdmin
        )

    }

}