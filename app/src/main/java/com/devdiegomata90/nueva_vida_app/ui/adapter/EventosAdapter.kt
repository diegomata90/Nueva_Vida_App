package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.viewholder.EventosViewHolder


class EventosAdapter(
    private val eventos: List<Evento>,
    val onItemClickListener: EventosViewHolder.onItemClickListener
) :
    RecyclerView.Adapter<EventosViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return EventosViewHolder(view,onItemClickListener)
    }

    override fun onBindViewHolder(holder: EventosViewHolder, position: Int) {
        val evento = eventos[position]
        holder.bind(evento)

    }


    override fun getItemCount()=eventos.size

}


