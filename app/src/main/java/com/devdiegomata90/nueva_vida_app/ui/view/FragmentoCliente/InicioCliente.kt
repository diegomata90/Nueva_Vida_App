package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Categoria

import com.devdiegomata90.nueva_vida_app.ui.view.Audio.AudioActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.ui.adapter.CategoriasAdapter
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.google.firebase.database.*
import com.devdiegomata90.nueva_vida_app.ui.view.Biblia.BibliaActivity
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.InicioClienteViewModel

@Suppress("DEPRECATION")
class InicioCliente : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var CategoriasTXT: TextView
    private lateinit var OtrasCategoriasTXT: TextView
    private lateinit var Evento: TextView
    private lateinit var Biblia: TextView
    private lateinit var Audio: TextView
    private lateinit var Video: TextView
    private lateinit var cardEvento: CardView
    private lateinit var cardBiblia: CardView
    private lateinit var cardAudio: CardView
    private lateinit var cardVideo: CardView
    private lateinit var versiculodiaTXT: TextView
    private lateinit var versiculodia: TextView
    private lateinit var libro: TextView
    private lateinit var capitulo: TextView
    private lateinit var btnCompartir: AppCompatImageView

    private lateinit var categoriaList: List<Categoria>

    private val inicioClienteViewModel: InicioClienteViewModel by viewModels() //Inicializa el ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //inflar la vista
        val view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false)

        initComponent(view)
        initRecyclerView()

        eventos()

        //Inicializa la funcion Oncreate para el viewModel
        inicioClienteViewModel.Oncreate()


        // Observa cambios en el StateFlow de categorías
        //Recupera las categorias y agragar la lista al reciclerView
        lifecycleScope.launchWhenStarted {
            inicioClienteViewModel.categories.collect { categories ->
                categoriasAdapter.updateData(categories as List<Categoria>)
            }
        }


        inicioClienteViewModel.dailyVerse.observe( this, Observer { dailyVerse ->
            if (dailyVerse != null) {
                libro.text = dailyVerse.libro
                capitulo.text = "${dailyVerse.capitulo}:${dailyVerse.versiculo}"
                versiculodia.text = dailyVerse.texto

            }
        })

        // Asigna Tipo Letra de Ubuntu a los texView (funcion Disena para usar en cualquier activity)
        TypefaceUtil.asignarTipoLetra(
            view.context,
            null,
            CategoriasTXT, OtrasCategoriasTXT,
            Evento, Biblia, Audio, Video,
            versiculodiaTXT,
        )

        TypefaceUtil.asignarTipoLetra(
            view.context,
            "fons/Caveat-Medium.ttf",
            libro,
            capitulo,
            versiculodia
        )

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
        versiculodiaTXT = view.findViewById(R.id.versiculodiaTXT)
        libro = view.findViewById(R.id.libro)
        capitulo = view.findViewById(R.id.Capitulo)
        versiculodia = view.findViewById(R.id.versiculodia)
        btnCompartir = view.findViewById(R.id.btnCompartir)

    }

    private fun initRecyclerView() {
        categoriaList = emptyList()

        // Configurar el adaptador de la lista
        categoriasAdapter = CategoriasAdapter(
            categoriaList
            //categoriaList = categoriaList
            //onClickListener = { versiculo -> getVersiculo(versiculo) }
        )
        rvCategorias.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCategorias.adapter = categoriasAdapter
    }

    private fun eventos() {

        cardEvento.setOnClickListener {
            startActivity(Intent(requireContext(), EventoActivity::class.java))
        }
        cardBiblia.setOnClickListener {
            startActivity(Intent(requireContext(), BibliaActivity::class.java))
        }
        cardAudio.setOnClickListener {
            startActivity(Intent(requireContext(), AudioActivity::class.java))
        }
        cardVideo.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Ya casi esta listo los VIDEO, porfavor esperar",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnCompartir.setOnClickListener {

            val capitulo = capitulo.text
            val libro = libro.text
            val versiculo= versiculodia.text

            if(capitulo != null && libro != null) {
                val message ="$libro $capitulo RVR1960\n$versiculo\n\n${getText(R.string.app_name)}"

                inicioClienteViewModel.shareText(requireContext(),message)

            }

        }

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            //
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(view!!.context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                //
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}



