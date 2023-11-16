package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.model.Categoria
import com.devdiegomata90.nueva_vida_app.ui.viewholder.CategoriasViewHolder


class CategoriasAdapter(private val categorias: List<Categoria>):
    RecyclerView.Adapter<CategoriasViewHolder> (){

    //crea una vista visual, montar la vista para el metodo getItemcount pueda pasarle la informacion a pintar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false)
        return CategoriasViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
          holder.render(categorias[position])
    }


    //Retorna el tamano del lista de items
    override fun getItemCount() = categorias.size



}