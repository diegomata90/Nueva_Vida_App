package com.devdiegomata90.nueva_vida_app.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R

import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil


class TextBiblicoViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    //Pintamos las vista
    private val versiculoTexto: TextView = itemView.findViewById(R.id.versiculoTexto)
    private val nVersiculo: TextView = itemView.findViewById(R.id.nversiculo)

    fun bind(versiculo: VersesResponse, onClickListener: (VersesResponse) -> Unit) {
        // Limpia cualquier contenido previo en los TextViews
        nVersiculo.text = ""
        versiculoTexto.text = ""

        // Utiliza una expresión regular para encontrar el número al inicio del texto
        val regex = Regex("^\\d+")
        val matchResult = regex.find(versiculo.cleanText)

        if (matchResult != null) {
            // Extrae el número y el texto
            val numero = matchResult.value
            val texto = versiculo.cleanText.substring(numero.length)

            // Asigna el número y el texto a los TextViews
            nVersiculo.text = numero
            versiculoTexto.text = texto
        } else {
            // Si no se encuentra un número, simplemente asigna el texto a versiculoTexto
            versiculoTexto.text = versiculo.cleanText
        }

        itemView.setOnClickListener{ onClickListener(versiculo) }

        TypefaceUtil.asignarTipoLetra(itemView.context,null,nVersiculo,versiculoTexto)

    }

}