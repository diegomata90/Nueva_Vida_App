package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.LoadingDialog
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.databinding.ActivityAudioAgregarBinding
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAgregarViewModel
import com.squareup.picasso.Picasso
import java.util.*


class AudioAgregarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioAgregarBinding
    private val audioAgregarViewModel: AudioAgregarViewModel by viewModels()
    private var RutaArchivoUri: Uri? = null
    private var RutaArchivoUriAudio: Uri? = null
    private lateinit var extensionImagen: String
    private lateinit var extensionAudio: String
    private lateinit var audioGet: Audio

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioAgregarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Audio")

        //Obtener los datos
        getIntents()

        //Funciones
        initComponent()
        eventoBoton()


        //Inicializa la funcion Oncreate para el viewModel
        audioAgregarViewModel.onCreate()

        //Suscribirse al ViewModel
        //Mostrar el mensaje al agregar
        audioAgregarViewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        audioAgregarViewModel.loading.observe(this) { loading ->
            if (loading) {
                loadingDialog.starLoading()
            } else {
                loadingDialog.isDismiss()
            }
        }

        //Si se agregago el audio lo enviar al menu principal
        audioAgregarViewModel.successful.observe(this) { successful ->
            if (successful) {
                //Enviar a la menu principal
                startActivity(Intent(this, AudioaActivity::class.java))
                finish()
            }
        }

    }

    private fun getDateToday(): String {
        //Obtener la fecha de hoy
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val fecha = "$day-$month-$year"

        return fecha
    }

    private fun initComponent() {
        // Inicializa la instancia del LoadingDialog
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Guardando imagen por favor espere..."
        loadingDialog.setCancelable = false

    }

    //Recibir el intent de actualizacion
    private fun getIntents() {
        audioGet = Audio()

        //Se obtiene los valores enviados por el intent
        val intent = intent.extras

        //Validar si el intent tiene valor null
        if (intent != null) {

            //Se cambia el titulo del Actionbar personalizado
            supportActionBar!!.title = "Actualizar Audio"

            //Se cambiar el titulo de la activity
            binding.AudioTXT.text = "Actualizar Audio"

            //Cambiar el nombre al boton
            binding.BotonAudio.text = "Actualizar"


            //Se obtiene los valores del intent en una variable

            val titulo = intent.getString("titulo")
            val imagen = intent.getString("imagen")
            val url = intent.getString("url")
            val id = intent.getString("id")
            val fecha = intent.getString("fecha")
            val uid = intent.getString("uid")
            val descripcion = intent.getString("descripcion")


            //Se crea un objeto de tipo audio para agregar los valores a la variable
            val aGets = Audio()
            aGets.titulo = titulo
            aGets.imagen = imagen
            aGets.url = url
            aGets.id = id
            aGets.fecha = fecha
            aGets.uid = uid
            aGets.descripcion = descripcion

            //Se actualiza la variable audioGet
            audioGet = aGets


            //Asignar los valores a la XML
            binding.run {
                TituloAudio.setText(titulo)
                DescripcionAudio.setText(descripcion)
                NombreAudioTxt.text = titulo
                NombreAudioTxt.visibility = View.VISIBLE

            }

            //Seteo de imagen
            try {
                Picasso.get().load(imagen).into(binding.imagenAgregarAudio)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar la imagen: " + e.message, Toast.LENGTH_SHORT)
                    .show()
                Picasso.get().load(R.drawable.categoria).into(binding.imagenAgregarAudio)
            }

        }


    }


    private fun eventoBoton() {
        binding.imagenAgregarAudio.setOnClickListener {
            seleccionarImagenDeGaleria()
        }

        binding.icoAgregarAudio.setOnClickListener {
            seleccionarAudioDeGaleria()
        }

        binding.BotonAudio.setOnClickListener {
            if (binding.BotonAudio.text == "Publicar") {
                addAudio()
            } else if (binding.BotonAudio.text == "Actualizar") {
                updateAudio()
            }
        }
    }

    private fun addAudio() {
        val audioAgregar = Audio()

        // Añade registros de depuración para verificar
        Log.d("addAudio", "RutaArchivoUri: $RutaArchivoUri")
        // Añade registros de depuración para verificar
        Log.d("addAudio", "RutaArchivoUriAudio: $RutaArchivoUriAudio")

        //Validar si los campos tiene valore
        if (binding.TituloAudio.text.toString().isEmpty()
            || binding.DescripcionAudio.text.toString().isEmpty()
            || binding.TituloAudio.text.toString().isBlank()
            || binding.DescripcionAudio.text.toString().isBlank()
        ) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
        } else if (RutaArchivoUri == null || RutaArchivoUriAudio == null
        ) {
            Toast.makeText(this, "Agregue un audio o una imagen", Toast.LENGTH_SHORT).show()
        } else {
            //SETEO de los valores
            audioAgregar.titulo = binding.TituloAudio.text.toString()
            audioAgregar.descripcion = binding.DescripcionAudio.text.toString()

            audioAgregar.fecha = getDateToday()

            //Asignar el URL de la imagen y audio
            audioAgregar.imagenUri = RutaArchivoUri.toString()
            audioAgregar.audioUri = RutaArchivoUriAudio.toString()

            //Asignar las extensiones a la variable audioAgregar
            audioAgregar.extentionAudio = extensionAudio
            audioAgregar.extentionImagen = extensionImagen

            audioAgregarViewModel.addAudio(audioAgregar)
        }
    }


    //Para actualizar el audio
    private fun updateAudio() {

        // Seteo de los valores de la variable audioGet
        audioGet.titulo = binding.TituloAudio.text.toString()
        audioGet.descripcion = binding.DescripcionAudio.text.toString()

        Log.d("updateAudioV1", "audioGet.imagenUri: ${audioGet.imagenUri}")
        Log.d("updateAudioV1", "audioGet.audioUri: ${audioGet.audioUri}")

        // Agregar igual el url con la imagenUri si es NULO
        if (audioGet.imagenUri == null || audioGet.imagenUri == "" &&
            audioGet.audioUri == null || audioGet.audioUri == "")
        {
            audioGet.imagenUri = audioGet.imagen
            audioGet.audioUri = audioGet.url
        }//Validar si imagenUri diferente a imagen y audioUri es diferente a url
        else if( audioGet.imagenUri != audioGet.imagen && audioGet.audioUri != audioGet.url) {
            audioGet.imagen = null
            audioGet.url = null
        }

        Log.d("updateAudioV", "audioGet.imagenUri: ${audioGet.imagenUri}")
        Log.d("updateAudioV", "audioGet.audioUri: ${audioGet.audioUri}")

        //Validar si los campos tiene valores
        if (binding.TituloAudio.text.toString().isEmpty() ||
            binding.DescripcionAudio.text.toString().isEmpty() ||
            binding.TituloAudio.text.toString().isBlank() ||
            binding.DescripcionAudio.text.toString().isBlank()
        ) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
        }
        else if (audioGet.imagenUri == null || audioGet.audioUri == null) {
            Toast.makeText(this, "Agregue un audio o una imagen", Toast.LENGTH_SHORT).show()
        }
        else {
            audioGet.fecha = getDateToday()

            //envia los dato recibido al ViewModel para la actualizacion
            audioAgregarViewModel.updateAudio(audioGet)
        }


    }



    private fun seleccionarImagenDeGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        obtenerImagenGaleria.launch(intent)
    }

    private val obtenerImagenGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Manejar el resultado de nuestro intent
            if (result.resultCode == Activity.RESULT_OK) {
                // Selección de imagen
                val data: Intent? = result.data

                // Obtener URI de la imagen
                RutaArchivoUri = data?.data!!


                //Validar si existe el objeto audioGet
                if (audioGet != null)audioGet.imagenUri = RutaArchivoUri.toString()


                // Mostrar la imagen en ImageView
                binding.imagenAgregarAudio.setImageURI(RutaArchivoUri)

                // Obtener la extension de la ImagenView
                extensionImagen = getExtension(RutaArchivoUri!!)!!

                 if (audioGet != null) audioGet.extentionImagen = extensionImagen

            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun seleccionarAudioDeGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        obtenerAudioGaleria.launch(intent)
    }

    private val obtenerAudioGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Manejar el resultado de nuestro intent
            if (result.resultCode == RESULT_OK) {
                // Selección de audio
                val data: Intent? = result.data

                // Obtener URI de la audio
                RutaArchivoUriAudio = data?.data!!


                // Validar si existe el objeto audioGet
                if(audioGet != null) audioGet.audioUri = RutaArchivoUriAudio.toString()


                // Obtener el nombre del archivo
                if (RutaArchivoUriAudio != null) {
                    val fileName = contentResolver.getFileName(RutaArchivoUriAudio!!)
                    binding.NombreAudioTxt.text = fileName
                    binding.NombreAudioTxt.visibility = View.VISIBLE

                    // Obtener la extension del archivo
                    extensionAudio = getExtension(RutaArchivoUriAudio!!)!!

                    // Validar si existe el objeto audioGet
                    if(audioGet != null) audioGet.extentionAudio = extensionAudio

                }

                // Mostrar la audio en ImageView
                binding.icoAgregarAudio.setImageResource(R.drawable.audio_select_ico)

            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    //Para obtener el nombre del URL
    private fun ContentResolver.getFileName(uri: Uri): String? {
        var name: String? = null
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    //Metodo para obtener la extension del archivo
    private fun getExtension(uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }


    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String) {

        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!!          // CREAMOS EL ACTIONBAR
        actionBar.title = titulo                   // LE ASINAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true)

    }

    //Regresar al actividad anterior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}

