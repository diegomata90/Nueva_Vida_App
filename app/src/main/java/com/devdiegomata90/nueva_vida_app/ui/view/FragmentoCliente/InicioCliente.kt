package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.ui.adapter.CategoriasAdapter
import com.devdiegomata90.nueva_vida_app.ui.view.Audio.AudioActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Biblia.BibliaActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.ui.view.OtrasCategorias.OtrasCategoriasActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Video.VideoActivity
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.InicioClienteViewModel
import com.google.firebase.database.*

@Suppress("DEPRECATION")
class InicioCliente : Fragment() {

    private lateinit var rvCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
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
    private lateinit var ConConexion: LinearLayoutCompat
    private lateinit var SinConexion: LinearLayoutCompat
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback


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
        askNotificationPermission(view)

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


        inicioClienteViewModel.dailyVerse.observe( viewLifecycleOwner, Observer { dailyVerse ->
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
        ConConexion = view.findViewById(R.id.ConConexion)
        SinConexion = view.findViewById(R.id.SinConexion)
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                requireActivity().runOnUiThread {
                    ConConexion.setVisibility(View.VISIBLE)
                    SinConexion.setVisibility(View.GONE)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                requireActivity().runOnUiThread {
                    ConConexion.setVisibility(View.GONE)
                    SinConexion.setVisibility(View.VISIBLE)
                }
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }



    private fun initComponent(view: View) {
        rvCategorias = view.findViewById(R.id.rvCategorias)
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
            categoriaList,
            onClickListener = { categoriaList -> getOtraCategoria(categoriaList) }
        )
        rvCategorias.layoutManager =LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCategorias.adapter = categoriasAdapter
    }

    private fun getOtraCategoria(categoriaList: Categoria) {
        //Mostrar la categoria seleccionada
        Toast.makeText(requireContext(),"Haz elegido: ${categoriaList.nombre}",Toast.LENGTH_SHORT).show()

        //Enviar a otra activity para la categoria seleccionada
        val intent = Intent(requireContext(), OtrasCategoriasActivity::class.java)
        intent.putExtra("NombreCategoria", categoriaList.nombre.toString())
        startActivity(intent)
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
            startActivity(Intent(requireContext(), VideoActivity::class.java))
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

    private fun askNotificationPermission(view: View) {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.POST_NOTIFICATIONS) ==
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




