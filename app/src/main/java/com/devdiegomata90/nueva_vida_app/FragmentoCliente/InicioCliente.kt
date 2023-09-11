package com.devdiegomata90.nueva_vida_app.FragmentoCliente

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.devdiegomata90.nueva_vida_app.R

class InicioCliente : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false)

        //Crear tipo letra personalizado
        var typefaceCaveat = Typeface.createFromAsset(context?.assets,"fons/Caveat-Medium.ttf")
        var typefaceUbuntu = Typeface.createFromAsset(context?.assets,"fons/UbuntuCondensed-Regular.ttf")

        var inicio_cliente = view.findViewById<TextView>(R.id.inicio_cliente)

        inicio_cliente.typeface = typefaceUbuntu

        return view
    }

}