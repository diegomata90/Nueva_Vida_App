package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.ui.viewholder.OtrasCategoriasViewHolder

class OtrasCategoriasAdapter(
    private var oCategoriaList: List<Categoria>,
    private val onClickListener: (Categoria) -> Unit
) : RecyclerView.Adapter<OtrasCategoriasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtrasCategoriasViewHolder {

        //Conectamos la vista con el XML
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_otras_categorias, parent, false)
        return OtrasCategoriasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return oCategoriaList.size
    }

    override fun onBindViewHolder(holder: OtrasCategoriasViewHolder, position: Int) {

        val oCategoria = oCategoriaList[position]
        holder.bind(oCategoria, onClickListener)
    }

    fun updateData(newOCategoriaList: List<Categoria>) {
        oCategoriaList = newOCategoriaList
        notifyDataSetChanged()
    }
}