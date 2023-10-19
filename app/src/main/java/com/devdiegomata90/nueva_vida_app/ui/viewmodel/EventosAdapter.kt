package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.view.EventoA.EventoaActivity


class EventosAdapter(
    private val eventos: List<Evento>,
    private val isUserAuthenticated: Boolean
    ) :
    RecyclerView.Adapter<EventosViewHolder>() {
    var eventoClickListener: EventoaActivity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return EventosViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventosViewHolder, position: Int) {
        val evento = eventos[position]
        holder.bind(evento,isUserAuthenticated)

        holder.itemView.setOnClickListener {
            eventoClickListener?.onUpdateEventoClick(evento)
        }
    }


    override fun getItemCount()=eventos.size

}


