package com.devdiegomata90.nueva_vida_app.ui.view.Audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventosAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AudioActivity : AppCompatActivity() {

    //Variable de la XML
    private lateinit var rvAudio:RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    private lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Audio")

        initComponent()
        initUi()
    }



    private fun initComponent() {
        rvAudio = findViewById(R.id.rvAudio)
        databaseReference = FirebaseDatabase.getInstance().getReference("AUDIOS")
    }

    private fun initUi() {
        val query =
            databaseReference.orderByChild("titulo") // Ordenar por el campo "titulo" si es necesario
        val audioList = ArrayList<Audio>()

        // Obtenemos la fecha de hoy
        val currentDate = Date()


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                audioList.clear()
                for (snapshot in dataSnapshot.children) {
                    val audio = snapshot.getValue(Audio::class.java)
                    if (audio != null) {
                        audioList.add(audio)
                    }
                }

                //Parte 1 Apartador : Conecta toda la informacion con reciclyView
                audioAdapter = AudioAdapter(audioList)
                rvAudio.layoutManager =
                    LinearLayoutManager(this@AudioActivity, LinearLayoutManager.VERTICAL, false)
                rvAudio.adapter = audioAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    this@AudioActivity,
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