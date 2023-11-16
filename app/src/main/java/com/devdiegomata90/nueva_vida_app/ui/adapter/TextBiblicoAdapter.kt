package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.network.VersesResponse
import com.devdiegomata90.nueva_vida_app.ui.viewholder.TextBiblicoViewHolder

class TextBiblicoAdapter(
    private var versiculoList: List<VersesResponse>,
    private val onClickListener: (VersesResponse) -> Unit,
) : RecyclerView.Adapter<TextBiblicoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextBiblicoViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_versiculo, parent, false)
        return TextBiblicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextBiblicoViewHolder, position: Int) {

        val versiculo = versiculoList[position]
        holder.bind(versiculo, onClickListener)

    }

    override fun getItemCount(): Int {
        return versiculoList.size
    }

    fun updateData(newVersiculoList: List<VersesResponse>) {
        versiculoList = newVersiculoList
        notifyDataSetChanged()
    }


}