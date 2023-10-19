package com.devdiegomata90.nueva_vida_app.ui.view.Evento

import android.content.Intent
import android.media.metrics.Event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.view.EventoA.EventoAgregarActivity
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosAdapter
import com.google.firebase.database.*


class EventoActivity : AppCompatActivity() {
    private lateinit var crearEvento:Button

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

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventoList.clear()
                for (snapshot in dataSnapshot.children) {
                    val evento = snapshot.getValue(Evento::class.java)
                    if (evento != null) {
                        eventoList.add(evento)
                    }
                }

                //Parte 1 Apartador : Conecta toda la informacion con reciclyView
               eventosAdapter = EventosAdapter(eventoList,false)
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

}