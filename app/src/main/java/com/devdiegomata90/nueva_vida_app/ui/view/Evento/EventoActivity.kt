package com.devdiegomata90.nueva_vida_app.ui.view.Evento


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosViewHolder
import com.google.firebase.database.*


import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class EventoActivity : AppCompatActivity(), EventosViewHolder.onItemClickListener  {

    //Variable de la XML
    private lateinit var rvEvento:RecyclerView
    private lateinit var eventosAdapter: EventosAdapter
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Evento")

        initComponent()
        initUi()

    }

    private fun initComponent() {
        rvEvento = findViewById(R.id.rvEventos)
        databaseReference = FirebaseDatabase.getInstance().getReference("EVENTOS")
    }


    private fun initUi() {
        val query =
            databaseReference.orderByChild("titulo") // Ordenar por el campo "titulo" si es necesario
        val eventoList = ArrayList<Evento>()

        // Obtenemos la fecha de hoy
        val currentDate = Date()


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventoList.clear()
                for (snapshot in dataSnapshot.children) {
                    val evento = snapshot.getValue(Evento::class.java)
                    if (evento != null) {
                        //Convierte la fecha formato de string a Date
                        val eventDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            .parse(evento.fecha) // Parsea la fecha de la base de datos

                        val eventTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                            .parse(evento.hora) // Parsea la hora de la base de datos

                        if(eventDate != null && eventTime != null) {
                            // Combina la fecha y la hora en un objeto de tipo Date
                            val eventDateTime = Date(eventDate.time + eventTime.time)

                            // Variables para obtener la fecha de hoy
                            if (eventDateTime >= currentDate)  {
                                eventoList.add(evento)
                                Log.i("Current date", "Fecha Evento ${evento.titulo}: " + eventDateTime.toString() + " Fecha actual: " +currentDate )
                            }
                        }

                    }
                }

                //Parte 1 Apartador : Conecta toda la informacion con reciclyView
               eventosAdapter = EventosAdapter(eventoList,this@EventoActivity)
                rvEvento.layoutManager =
                    LinearLayoutManager(this@EventoActivity, LinearLayoutManager.VERTICAL, false)
                rvEvento.adapter = eventosAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    this@EventoActivity,
                    "Error en Firebase: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }



    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String){
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!!          // CREAMOS EL ACTIONBAR
        actionBar.title = titulo                   // LE ASINAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //Cuando se le da un click al evento
    override fun onClick(position: Int) {
        Toast.makeText(this, "OnClick CLIENTE de la vista #${position.toString()}", Toast.LENGTH_SHORT).show()

    }

    //Cuando se deja precionado el evento
    override fun onLongClick(evento: Evento) {
        Toast.makeText(this, "OnLongClick CLIENTE del Evento: ${evento.titulo}", Toast.LENGTH_SHORT).show()
    }

}

