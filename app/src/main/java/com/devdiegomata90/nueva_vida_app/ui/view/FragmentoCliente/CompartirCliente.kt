package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil

class CompartirCliente : Fragment() {

    private lateinit var btnCompartir: Button
    private lateinit var CompartirTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_compartir_cliente, container, false)


        btnCompartir = view.findViewById(R.id.ButonCompartir)
        CompartirTxt = view.findViewById(R.id.CompartirTxt)

        evento()

        TypefaceUtil.asignarTipoLetra(view.context, null, btnCompartir,CompartirTxt)

        return view
    }

    fun evento(){

        btnCompartir.setOnClickListener {
            Toast.makeText(requireContext(),"Se agrega la funcionalidad cuando este publico", Toast.LENGTH_SHORT).show()
        }

    }


}