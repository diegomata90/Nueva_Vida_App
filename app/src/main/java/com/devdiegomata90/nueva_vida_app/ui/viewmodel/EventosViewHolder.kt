package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.ui.view.EventoA.EventoaActivity
import com.squareup.picasso.Picasso


class EventosViewHolder(private val view: View):RecyclerView.ViewHolder(view) {

    //Pintamos las vista
    private var tituloEvento:TextView = view.findViewById(R.id.TituloEvento)
    private var fechaEvento:TextView = view.findViewById(R.id.FechaEvento)
    private val lugarEvento:TextView = view.findViewById(R.id.LugarEvento)
    private val horaEvento:TextView = view.findViewById(R.id.HoraEvento)
    private val imageEvento:ImageView =view.findViewById(R.id.imageEvento)
    val VIEW = view

    //METODO PARA ALMANCENAR LA ACCION DEL ADMINISTRADOR

    interface EventoClickListener {
        fun onEventoClick(evento: Evento)
        fun onEliminarEventoClick(evento: Evento)
    }

    fun bind(evento: Evento, isUserAuthenticated: Boolean) {

        //Pintamos los datos
        render(evento)

        //Damos un click al evento
        // Agregar un OnClickListener a la vista
        VIEW.setOnClickListener {
            // Llamar al método onEventoClick() de la actividad
            (VIEW.context as? EventoaActivity)?.onEventoClick(evento)

            // Mostrar un mensaje cuando se hace clic
            Toast.makeText(VIEW.context, "Has hecho clic en el evento ${evento.titulo}", Toast.LENGTH_SHORT).show()

            // Agregar una línea de registro para ver si se llega a este punto
            Log.d("Click", "Se hizo clic en un elemento del RecyclerView")
            //true
        }

        // Verifica la autenticación antes de mostrar las opciones del onLongClick
       view.setOnLongClickListener {
            if (isUserAuthenticated) {
                // Agregar una línea de registro para ver si se llega a este punto
                Log.d("LongClick", "Se hizo LongClic en un elemento del RecyclerView")

                // Mostrar las opciones
                val opciones = arrayOf("Actualizar", "Eliminar")

                val builder = AlertDialog.Builder(view.context)

                builder.setTitle("Selecciona la acción")
                    .setItems(opciones) { _, which ->
                        when (which) {
                            0 -> {
                                (view.context as? EventoaActivity)?.onUpdateEventoClick(evento)
                            }
                            1 -> {
                                (view.context as? EventoaActivity)?.onEliminarEventoClick(evento)
                            }
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }
            else {
                Toast.makeText(view.context, "Evento: ${evento.titulo}", Toast.LENGTH_SHORT).show()

                // Agregar una línea de registro para ver si se llega a este punto
                Log.d("ClienteClick", "Se hizo ClienteLogClic en un elemento del RecyclerView")
            }

            true
        }

    }

  /*  init {
        view.setOnLongClickListener {
            val opciones = arrayOf("Actualizar", "Eliminar")

            val builder = AlertDialog.Builder(view.context)

            builder.setTitle("Selecciona la acción")
                .setItems(opciones) { _, which ->
                    when (which) {
                        0 -> {
                            (view.context as? EventoaActivity)?.onUpdateEventoClick(eventoArray)
                        }
                        1 -> {
                            (view.context as? EventoaActivity)?.onEliminarEventoClick(eventoArray)
                        }
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()

            true
        }
    }

   */

    fun render(eventos: Evento){

        //Asigna texto de basedatos al textView
        tituloEvento.text = eventos.titulo
        fechaEvento.text = eventos.fecha
        lugarEvento.text = eventos.lugar
        horaEvento.text = eventos.hora
        val imagen = eventos.imagen


        // Dar formato a la fecha "05 de octubre del 2023"
        val fechaSinFormato = eventos.fecha.toString()
        val fechaFormateada = formatFecha(fechaSinFormato)
        fechaEvento.text = fechaFormateada



        ///Capturar la imagen con libreria Picaso
        ///Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(imageEvento)
        } catch (e: Exception) {
            //Si la imagen no fue traida exitosamente
            Picasso.get().load(R.drawable.categoria).into(imageEvento)
        }
        
        
    }

      private fun formatFecha(fechaSinFormato: String): String {
        // Dividir la fecha en sus componentes
        val partes = fechaSinFormato.split("-")
        if (partes.size != 3) {
            // Manejar un formato de fecha incorrecto (debería ser dd-mm-yyyy)
            return fechaSinFormato
        }

        val dia = partes[0]
        val mesNumerico = partes[1]
        val anio = partes[2]

        // Convertir el componente del mes a su representación textual en español
        val meses = arrayOf(
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        val mesTexto = if (mesNumerico.toIntOrNull() in 1..12) {
            meses[mesNumerico.toInt() - 1]
        } else {
            // Manejar un mes inválido
            return fechaSinFormato
        }

        // Formatear la fecha en el nuevo formato
        return "$dia de $mesTexto del $anio"

}


}





