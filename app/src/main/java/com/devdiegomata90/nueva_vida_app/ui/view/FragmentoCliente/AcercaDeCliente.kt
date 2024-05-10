package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil

class AcercaDeCliente : Fragment() {

    private lateinit var acercade: TextView
    private lateinit var desarrollador: TextView
    private lateinit var correoDev: TextView
    private lateinit var versionApp: TextView
    private lateinit var redesSocial: TextView
    private lateinit var txtIrWebsite: TextView
    private lateinit var txtIrLinkedin: TextView
    private lateinit var txtIrTelegram: TextView
    private lateinit var txtIrYouTube: TextView
    private lateinit var txtIrGithub: TextView
    private lateinit var txtIrCorreo: TextView
    private lateinit var logon: AppCompatImageView
    private lateinit var infoContact: TextView

    private lateinit var irWebsite: LinearLayoutCompat
    private lateinit var irLinkedin: LinearLayoutCompat
    private lateinit var irTelegram: LinearLayoutCompat
    private lateinit var irYouTube: LinearLayoutCompat
    private lateinit var irGithub: LinearLayoutCompat
    private lateinit var irCorreo: LinearLayoutCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_acerca_de_cliente, container, false)

        initComponent(view)
        eventos()

        TypefaceUtil.asignarTipoLetra(
            view.context,
            null,
            acercade, desarrollador, correoDev, versionApp,
            txtIrWebsite,txtIrLinkedin,txtIrTelegram,txtIrYouTube,txtIrGithub,txtIrCorreo,
            redesSocial,infoContact, irWebsite, irLinkedin, irTelegram, irYouTube, irGithub,irCorreo
        )

        return view
    }

    private fun initComponent(view: View) {

        logon =view.findViewById(R.id.logon)
        acercade = view.findViewById(R.id.AcercaDeApp)
        desarrollador = view.findViewById(R.id.desarrollador)
        correoDev = view.findViewById(R.id.InfoCorreo)
        versionApp = view.findViewById(R.id.InfoVersion)
        redesSocial = view.findViewById(R.id.InfoRedes)
        infoContact = view.findViewById(R.id.InfoContact)
        irWebsite = view.findViewById(R.id.Ir_website)
        irLinkedin = view.findViewById(R.id.Ir_linkedin)
        irTelegram = view.findViewById(R.id.Ir_telegram)
        irYouTube = view.findViewById(R.id.Ir_youtube)
        irGithub = view.findViewById(R.id.Ir_Github)
        irCorreo = view.findViewById(R.id.Ir_correo)

        txtIrWebsite = view.findViewById(R.id.txtIr_website)
        txtIrLinkedin = view.findViewById(R.id.txtIr_linkedin)
        txtIrTelegram = view.findViewById(R.id.txtIr_telegram)
        txtIrYouTube = view.findViewById(R.id.txtIr_youtube)
        txtIrGithub = view.findViewById(R.id.txtIr_Github)
        txtIrCorreo = view.findViewById(R.id.txtIr_correo)

    }
    private fun eventos(){

        //Evento para que cuando presionen se dirija la app desarrollador

        logon.setOnClickListener{
            irToApp("https://diegomata90.github.io/")
        }
        irWebsite.setOnClickListener{
            irToApp("https://diegomata90.github.io/")
        }

        irLinkedin.setOnClickListener{
            irToApp("https://www.linkedin.com/in/diego-mata-1ts1st3m45/")
        }

        irTelegram.setOnClickListener{
            irToApp("https://t.me/lmata90")
        }

        irYouTube.setOnClickListener{
            irToApp("https://www.youtube.com/channel/UCTFCnBecYOA5zU7xZq9ZPyA")
        }
        irGithub.setOnClickListener{
            irToApp("https://github.com/diegomata90")
        }
        irCorreo.setOnClickListener{
            irToApp("mailto:devdiegomata90@gmail.com")
        }
    }

    //funcion para enviar al aplicaion indicada
    private fun irToApp(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


}