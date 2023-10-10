package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosAdapter
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


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

    //Es metodo cuando selecion la opcion 0 modificar el opcion evento
    fun onUpdateEventoClick(evento: Evento) {
        // Maneja el clic en el evento aquí
        // Opción "Actualizar" seleccionada

        val context = this

        //Abre una activity y le envia los datos para actualizacion.
        val intent = Intent(context,EventoAgregarActivity::class.java)
        intent.putExtra("eventoId",evento.id)
        intent.putExtra("eventoTitulo",evento.titulo)
        intent.putExtra("eventoDescrip",evento.descripcion)
        intent.putExtra("eventoFecha",evento.fecha)
        intent.putExtra("eventoLugar",evento.lugar)
        intent.putExtra("eventoHora",evento.hora)
        intent.putExtra("eventoImagen",evento.imagen)

        context.startActivity(intent)
        finish()
    }

    //Es metodo cuando selecion la opcion 1 eliminar el opcion evento
    fun onEliminarEventoClick(evento: Evento) {

        // Maneja la eliminación del evento aquí
        val idEvento = evento.id

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Estás seguro que quieres eliminar este evento ${evento.titulo} ?")
            .setPositiveButton("SI") { dialog, _ ->
                // Realiza eliminacion de la imagen
                eliminarImagen(idEvento!!, evento.imagen!!)

                /* ELIMANAR IMAGEN DE LA BD */

                /* ELIMANAR IMAGEN DE LA BD */
                val query: Query =
                    databaseReference.orderByChild("id").equalTo(idEvento) // atributo de la basedatos peliculas

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    // Estar al pendiente de la iliminacion de la imagen
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //Recorre toda la base datos Musica y elimina el nombre elegido
                        for (ds in snapshot.children) {
                            ds.ref.removeValue()
                        }
                        Toast.makeText(
                            this@EventoaActivity,
                            "Datos borrador",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@EventoaActivity, error.message, Toast.LENGTH_SHORT).show()
                    }
                })
                /* ELIMANAR IMAGEN DEl STORAGE de la carpeta Pelicula_Subida*/
                val storageRef = FirebaseStorage.getInstance().reference
                storageRef.child("Eventos_Subidos/").child(evento.imagen!!).delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@EventoaActivity,
                            "Eliminado",
                            Toast.LENGTH_SHORT
                        ).show() }
                    .addOnFailureListener {e ->
                        Toast.makeText(
                            this@EventoaActivity,
                            "No se pudo eliminar -->$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }




                // Puedes realizar cualquier otro manejo necesario aquí, como actualizar la lista
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }



    private fun eliminarImagen(idEvento: String, imagen: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val ref = storageRef.child(imagen)
        ref.delete().addOnSuccessListener {
            Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show()
            eliminarDatos(idEvento)
        }.addOnFailureListener {e ->
            Toast.makeText(this, "No se pudo eliminar la imagen FFF-->$e", Toast.LENGTH_SHORT).show()

        }
    }

    private fun eliminarDatos(idEvento: String) {
        databaseReference.child("EVENTOS").child(idEvento).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {e ->
            Toast.makeText(this, "No se pudo eliminar el evento -->$e", Toast.LENGTH_SHORT).show()
        }
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