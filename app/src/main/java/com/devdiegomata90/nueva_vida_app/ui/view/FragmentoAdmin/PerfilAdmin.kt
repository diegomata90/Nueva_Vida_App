package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.devdiegomata90.nueva_vida_app.R


class PerfilAdmin : Fragment() {

    private lateinit var cambioPass: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perfil_admin, container, false)

        initComponent(view)
        eventos()

        return view
    }

    fun initComponent(view: View) {

        cambioPass = view.findViewById(R.id.CambioPass)

    }

    fun eventos(){

        cambioPass.setOnClickListener {

            startActivity(Intent(requireContext(), CambioPass::class.java))
        }
    }

}