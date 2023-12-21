package com.devdiegomata90.nueva_vida_app.ui.view.AudioA

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
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
import com.devdiegomata90.nueva_vida_app.ui.view.MainActivityAdmin
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.AudioAgregarViewModel
import java.util.*


class AudioAgregarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioAgregarBinding
    private val audioAgregarViewModel: AudioAgregarViewModel by viewModels()
    private lateinit var RutaArchivoUri: Uri
    private lateinit var RutaArchivoUriAudio: Uri
    private lateinit var extensionImagen: String
    private lateinit var extensionAudio: String
    private lateinit var extensiones: Pair<String, String>

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioAgregarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Audio")

        //Funciones
        initComponent()
        eventoBoton()


        //Inicializa la funcion Oncreate para el viewModel
        audioAgregarViewModel.Oncreate()

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
                startActivity(Intent(this, MainActivityAdmin::class.java))
                finish()
            }
        }

    }

    private fun initComponent() {
        // Inicializa la instancia del LoadingDialog
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Agregando valores por favor espere..."
        loadingDialog.setCancelable = false

    }



    private fun eventoBoton() {
        binding.imagenAgregarAudio.setOnClickListener {
            seleccionarImagenDeGaleria()
        }

        binding.icoAgregarAudio.setOnClickListener {
            seleccionarAudioDeGaleria()
        }

        binding.BotonAudio.setOnClickListener {
            val audioAgregar = Audio()

            audioAgregar.titulo = binding.TituloAudio.text.toString()
            audioAgregar.descripcion = binding.DescripcionAudio.text.toString()
            audioAgregar.imagen = ""
            audioAgregar.url = ""

            //Validar si los campos tiene valore
            if (binding.TituloAudio.text.toString()
                    .isEmpty() || binding.DescripcionAudio.text.toString().isEmpty() ||
                binding.TituloAudio.text.toString()
                    .isBlank() || binding.DescripcionAudio.text.toString().isBlank()
            ) {
                Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
            } else if (RutaArchivoUriAudio == null || RutaArchivoUri == null
            ) {
                Toast.makeText(this, "Agregue un audio o una imagen", Toast.LENGTH_SHORT).show()
            } else {
                //Obtener la fecha de hoy
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH) + 1
                val day = c.get(Calendar.DAY_OF_MONTH)
                val fecha = "$day-$month-$year"

                audioAgregar.fecha = fecha

                //Asignan las extensiones a la variable extension
                extensiones = extensionAudio to extensionImagen

                audioAgregarViewModel.addAudio(
                    audioAgregar, RutaArchivoUri, RutaArchivoUriAudio, extensiones
                )
            }
        }
    }


    private fun seleccionarImagenDeGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        ObtenerImagenGaleria.launch(intent)
    }

    private val ObtenerImagenGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Manejar el resultado de nuestro intent
            if (result.resultCode == Activity.RESULT_OK) {
                // Selección de imagen
                val data: Intent? = result.data
                // Obtener URI de la imagen
                RutaArchivoUri = data?.data!!
                // Mostrar la imagen en ImageView
                binding.imagenAgregarAudio.setImageURI(RutaArchivoUri)
                // Obtener la extension de la ImagenView
                extensionImagen = getExtension(RutaArchivoUri)!!
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun seleccionarAudioDeGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        ObtenerAudioGaleria.launch(intent)
    }

    private val ObtenerAudioGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Manejar el resultado de nuestro intent
            if (result.resultCode == RESULT_OK) {
                // Selección de audio
                val data: Intent? = result.data
                // Obtener URI de la audio
                RutaArchivoUriAudio = data?.data!!

                // Obtener el nombre del archivo
                if (RutaArchivoUriAudio != null) {
                    val fileName = contentResolver.getFileName(RutaArchivoUriAudio)
                    binding.icoAgregarAudioTxt.text = fileName
                    binding.icoAgregarAudioTxt.visibility = View.VISIBLE
                    // Obtener la extension del archivo
                    extensionAudio = getExtension(RutaArchivoUriAudio)!!
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
