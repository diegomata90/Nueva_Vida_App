package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.CategoriasAdapter
import com.google.firebase.database.*

class InicioCliente : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Crear tipo letra personalizado
        //val typefaceUbuntu = Typeface.createFromAsset(context?.assets,"fons/Ubuntu.ttf")
        //val inicio_cliente = view.findViewById<TextView>(R.id.inicio_cliente)
        //inicio_cliente.typeface = typefaceUbuntu

        return inflater.inflate(R.layout.fragment_inicio_cliente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent(view)
        initUi(view)
    }

    private fun initComponent(view: View) {
        rvCategorias = view.findViewById(R.id.rvCategorias)
        databaseReference = FirebaseDatabase.getInstance().getReference("Categorias")
    }

    private fun initUi(view: View) {

        val query =databaseReference.orderByChild("nombre") // Ordenar por el campo "nombre" si es necesario
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
                Toast.makeText(requireContext(), "Error en Firebase: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

}