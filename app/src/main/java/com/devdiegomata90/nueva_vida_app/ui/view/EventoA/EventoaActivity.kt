package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosAdapter
import com.google.firebase.database.*


class EventoaActivity : AppCompatActivity() {

    private lateinit var rvEventoA: RecyclerView
    private lateinit var eventosAdapter: EventosAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventoa)

        initComponent()
        initUi()


        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Evento")

    }

    private fun initComponent() {
        rvEventoA = findViewById(R.id.rvEventosA)
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
                eventosAdapter = EventosAdapter(eventoList)
                rvEventoA.layoutManager =
                    LinearLayoutManager(this@EventoaActivity, LinearLayoutManager.VERTICAL, false)
                rvEventoA.adapter = eventosAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    this@EventoaActivity,
                    "Error en Firebase: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //Agregarmo el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater // CREO OBJETO TIPO MenuInflater
        menuInflater.inflate(
            R.menu.menu_agregar,
            menu
        ) // INFLO EL MENU CREADO EN LLAMADO MENU_AGREGAR

        return super.onCreateOptionsMenu(menu)
    }

    //Direciona el menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.agregar -> {
                startActivity(Intent(this@EventoaActivity, EventoAgregarActivity::class.java))
                //finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String) {
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