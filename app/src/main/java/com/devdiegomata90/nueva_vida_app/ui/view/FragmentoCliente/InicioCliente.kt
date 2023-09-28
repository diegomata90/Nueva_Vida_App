package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.ui.view.MainActivityAdmin
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.CategoriasAdapter
import com.google.firebase.database.*

class InicioCliente : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var CategoriasTXT: TextView
    private lateinit var OtrasCategoriasTXT: TextView
    private lateinit var Evento:TextView
    private lateinit var Biblia:TextView
    private lateinit var Audio:TextView
    private lateinit var Video:TextView
    private lateinit var cardEvento:CardView
    private lateinit var cardBiblia:CardView
    private lateinit var cardAudio:CardView
    private lateinit var cardVideo:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflar la vista
        val view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false)

        initComponent(view)
        initUi(view)
        eventos()


        // Crear tipo letra personalizado (Ubuntu)
        val typefaceUbuntu = Typeface.createFromAsset(requireContext().assets, "fons/Ubuntu.ttf")


        // Aplicar la fuente Ubuntu a los elementos de texto
        CategoriasTXT.typeface = typefaceUbuntu
        OtrasCategoriasTXT.typeface = typefaceUbuntu
        Evento.typeface = typefaceUbuntu
        Biblia.typeface = typefaceUbuntu
        Audio.typeface = typefaceUbuntu
        Video.typeface = typefaceUbuntu

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initComponent(view: View) {
        rvCategorias = view.findViewById(R.id.rvCategorias)
        databaseReference = FirebaseDatabase.getInstance().getReference("Categorias")
        CategoriasTXT = view.findViewById(R.id.CategoriasTXT)
        OtrasCategoriasTXT = view.findViewById(R.id.OtrasCategoriasTXT)
        Evento = view.findViewById(R.id.Evento)
        Biblia = view.findViewById(R.id.Biblia)
        Audio = view.findViewById(R.id.Audio)
        Video = view.findViewById(R.id.Video)
        cardEvento = view.findViewById(R.id.cardEvento)
        cardBiblia = view.findViewById(R.id.cardBiblia)
        cardAudio = view.findViewById(R.id.cardAudio)
        cardVideo = view.findViewById(R.id.cardVideo)

    }

    private fun initUi(view: View) {

        val query =
            databaseReference.orderByChild("nombre") // Ordenar por el campo "nombre" si es necesario
        val categoriaList = ArrayList<Categoria>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categoriaList.clear()
                for (snapshot in dataSnapshot.children) {
                    val categoria = snapshot.getValue(Categoria::class.java)
                    if (categoria != null) {
                        categoriaList.add(categoria)
                    }
                }

                //Parte 1 Apartador : Conecta toda la informacion con reciclyView
                categoriasAdapter = CategoriasAdapter(categoriaList)
                rvCategorias.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                rvCategorias.adapter = categoriasAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    requireContext(),
                    "Error en Firebase: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun eventos() {

        cardEvento.setOnClickListener{
            startActivity(Intent(requireContext(), EventoActivity::class.java))
        }
        cardBiblia.setOnClickListener {
            Toast.makeText(requireContext(),"Ya casi esta listo la BIBLIA, porfavor esperar",Toast.LENGTH_SHORT).show()
        }
        cardAudio.setOnClickListener {
            Toast.makeText(requireContext(),"Ya casi esta listo los AUDIO, porfavor esperar",Toast.LENGTH_SHORT).show()
        }
        cardVideo.setOnClickListener {
            Toast.makeText(requireContext(),"Ya casi esta listo los VIDEO, porfavor esperar",Toast.LENGTH_SHORT).show()
        }

    }

}