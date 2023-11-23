package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.devdiegomata90.nueva_vida_app.ui.view.InicioSesion
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil

class LoginAdmin : Fragment() {
    lateinit var btnAcceder: Button
    private lateinit var SoloadminTXT: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_admin, container, false)

        btnAcceder = view.findViewById(R.id.btnAcceder)
        SoloadminTXT = view.findViewById(R.id.SoloadminTXT)

        btnAcceder.setOnClickListener {
            startActivity(Intent(requireContext(), InicioSesion::class.java))
        }

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disenada para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(view.context,null, SoloadminTXT,btnAcceder)

        return view
    }


}