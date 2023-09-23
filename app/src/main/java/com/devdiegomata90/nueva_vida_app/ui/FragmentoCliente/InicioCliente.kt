package com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R

class InicioCliente : Fragment() {

    private lateinit var rvCategory:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false)

        //Metodo para vint
        initComponent(view)
        initUi(view)

        //Crear tipo letra personalizado
        //val typefaceUbuntu = Typeface.createFromAsset(context?.assets,"fons/Ubuntu.ttf")
        //val inicio_cliente = view.findViewById<TextView>(R.id.inicio_cliente)
        //inicio_cliente.typeface = typefaceUbuntu

        return view
    }


    private fun initComponent(view: View){
        rvCategory =view.findViewById(R.id.rvCategory)
    }

    private fun initUi(view: View?) {

    }
}