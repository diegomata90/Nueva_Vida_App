package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoCliente

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.model.Categoria
import com.devdiegomata90.nueva_vida_app.network.VersesResponse

import com.devdiegomata90.nueva_vida_app.ui.view.Audio.AudioActivity
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.ui.adapter.CategoriasAdapter
import com.devdiegomata90.nueva_vida_app.util.TypefaceUtil
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.devdiegomata90.nueva_vida_app.api.VersesApiServe
import com.devdiegomata90.nueva_vida_app.ui.view.Biblia.BibliaActivity

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //inflar la vista
        val view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false)

        initComponent(view)
        initUi(view)
        eventos()

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
            versiculodia
        )

        /*
         ///Forma convencional para asignar tipo letra a los elementos de texto

        // Crear tipo letra personalizado (Ubuntu)
        val typefaceUbuntu = Typeface.createFromAsset(requireContext().assets, "fons/Ubuntu.ttf")

        // Aplicar la fuente Ubuntu a los elementos de texto
        CategoriasTXT.typeface = typefaceUbuntu
        OtrasCategoriasTXT.typeface = typefaceUbuntu
        Evento.typeface = typefaceUbuntu
        Biblia.typeface = typefaceUbuntu
        Audio.typeface = typefaceUbuntu
        Video.typeface = typefaceUbuntu
         */
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
        versiculodia = view.findViewById(R.id.versiculodia)

    }

    private fun initUi(view: View) {

        val query =
            databaseReference.orderByChild("nombre") // Ordenar por el campo "nombre" si es necesario
        val categoriaList = ArrayList<Categoria>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categoriaList.clear()
                for (snapshot in dataSnapshot.children) {
                    val categoria = snapshot.getValue(Categoria::class.java)
                    if (categoria != null) {
                        categoriaList.add(categoria)
                    }
                }

                //Parte 1 Apartador : Conecta toda la informacion con reciclyView
                categoriasAdapter = CategoriasAdapter(categoriaList)
                rvCategorias.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                rvCategorias.adapter = categoriasAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores si es necesario
                Toast.makeText(
                    requireContext(),
                    "Error en Firebase: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        searbyVerso("Gen","1:2")
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

    }

    // Metodo para crea la llama al retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/books/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Metodo con la corrutina
    private fun searbyVerso(book:String ,capver:String){
        val endpoint = "spa-RVR1960:$book/verses/$capver"

        lifecycleScope.launch {
            val baseUrl = "https://ajphchgh0i.execute-api.us-west-2.amazonaws.com/dev/api/books/"
            val fullUrl = baseUrl+endpoint

            val call = getRetrofit().create(VersesApiServe::class.java).getVerses("$fullUrl")
            val versiculos: List<VersesResponse>? = call.body()

            //Regresar el hilo principal
            Handler(requireContext().mainLooper).post{
                if(call.isSuccessful){
                    val primerVersiculo = versiculos?.firstOrNull() // Obtén el primer versículo de la lista

                    if (primerVersiculo != null) {

                        libro.text = primerVersiculo.capitulo ?: ""

                        // Utiliza una expresión regular para encontrar el número al inicio del texto
                        val regex = Regex("^\\d+")
                        val matchResult = regex.find(primerVersiculo.cleanText)

                        if (matchResult != null) {
                            // Extrae el número y el texto
                            versiculodia.text = primerVersiculo.cleanText.substring(matchResult.value.length)
                        }else{
                            versiculodia.text = primerVersiculo.cleanText ?: ""
                        }


                    } else {
                        showError()
                    }
                }
                else{
                    //error llamado
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Error mostrar versiculo", Toast.LENGTH_LONG).show()
    }


}



