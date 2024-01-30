package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.ui.view.EventoA.EventoaActivity
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.ui.view.AudioA.AudioaActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Biblia.BibliaActivity
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.InicioAdminViewModel

class InicioAdmin : Fragment() {

    private lateinit var CategoriasTXT: TextView
    private lateinit var OtrasCategoriasTXT: TextView
    private lateinit var NombreTXT: TextView
    private lateinit var Evento: TextView
    private lateinit var Biblia: TextView
    private lateinit var Audio: TextView
    private lateinit var Video: TextView
    private lateinit var cardEvento: CardView
    private lateinit var cardBiblia: CardView
    private lateinit var cardAudio: CardView
    private lateinit var cardVideo: CardView
    private val inicioAdminViewModel: InicioAdminViewModel by viewModels() //Inicializa el ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("InicioAdminFragment", "onCreateView called")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inicio_admin, container, false)

        initComponent(view)
        eventos()

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disena para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(
            view.context,
            null,
            CategoriasTXT,
            Evento,
            Biblia,
            Audio,
            Video
        )

        // Suscribirse a la view model
        //Oncreated
        inicioAdminViewModel.Oncreate()

        //Obtener los usuarios
        inicioAdminViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                NombreTXT.text = user.NOMBRES.toString() + " " + user.APELLIDOS.toString()
                Toast.makeText(
                    requireContext(),
                    "Bienvenido: ${user.NOMBRES.toString()} ${user.APELLIDOS.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        /*
        ///Forma convencional para asignar tipo letra a los elementos de texto

        // Crear tipo letra personalizado (Ubuntu)
        val typefaceUbuntu = Typeface.createFromAsset(requireContext().assets, "fons/Ubuntu.ttf")

        // Aplicar la fuente Ubuntu a los elementos de texto
        CategoriasTXT.typeface = typefaceUbuntu
        Evento.typeface = typefaceUbuntu
        Biblia.typeface = typefaceUbuntu
        Audio.typeface = typefaceUbuntu
        Video.typeface = typefaceUbuntu

         */

        return view
    }

    private fun initComponent(view: View) {

        CategoriasTXT = view.findViewById(R.id.CategoriasTXT)
        OtrasCategoriasTXT = view.findViewById(R.id.OtrasCategoriasTXT)
        NombreTXT = view.findViewById(R.id.NombreTXT)
        Evento = view.findViewById(R.id.Evento)
        Biblia = view.findViewById(R.id.Biblia)
        Audio = view.findViewById(R.id.Audio)
        Video = view.findViewById(R.id.Video)
        cardEvento = view.findViewById(R.id.cardEvento)
        cardBiblia = view.findViewById(R.id.cardBiblia)
        cardAudio = view.findViewById(R.id.cardAudio)
        cardVideo = view.findViewById(R.id.cardVideo)
    }

    private fun eventos() {
        cardEvento.setOnClickListener {
            startActivity(Intent(requireContext(), EventoaActivity::class.java))
        }
        cardBiblia.setOnClickListener {
            startActivity(Intent(requireContext(), BibliaActivity::class.java))
        }
        cardAudio.setOnClickListener {
            startActivity(Intent(requireContext(), AudioaActivity::class.java))
        }
        cardVideo.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Ya casi esta listo los VIDEO, porfavor esperar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}