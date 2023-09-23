package com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.devdiegomata90.nueva_vida_app.ui.InicioSesion
import com.devdiegomata90.nueva_vida_app.R

class LoginAdmin : Fragment() {
    lateinit var btnAcceder: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_admin, container, false)

        btnAcceder = view.findViewById(R.id.btnAcceder)

        btnAcceder.setOnClickListener {
            startActivity(Intent(requireContext(), InicioSesion::class.java))
        }

        return view
    }


}