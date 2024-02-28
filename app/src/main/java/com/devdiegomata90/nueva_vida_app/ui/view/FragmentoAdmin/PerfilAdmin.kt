package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.LoadingDialog
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.databinding.FragmentPerfilAdminBinding
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.PerfilAdminViewModel
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class PerfilAdmin : Fragment() {

    private lateinit var binding: FragmentPerfilAdminBinding
    private val perfilAdminViewModel: PerfilAdminViewModel by viewModels()
    private var rutaArchivoUri: Uri? = null
    private var extensionImagen: String? = null
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPerfilAdminBinding.inflate(inflater, container, false)
        val view = binding.root

        initComponent()
        eventos()

        //Inicializar el onCreate del ViewModel
        perfilAdminViewModel.onCreate()

        //Asignar los valores a los TextView
        perfilAdminViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.uidAdmin.text = user.UID
                binding.nombreAdmin.text = user.NOMBRES
                binding.apellidosAdmin.text = user.APELLIDOS
                binding.correoAdmin.text = user.CORREO

                val image = user.IMAGE
                try {
                    Picasso.get().load(image).placeholder(binding.imagenADMIN.drawable)
                        .into(binding.imagenADMIN)
                } catch (e: Exception) {
                    binding.imagenADMIN.setImageResource(R.drawable.perfil_ico)
                }

            }
        }

        //Escuchar el mensaje del ViewModel
        perfilAdminViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        //Escuchar el loading
        perfilAdminViewModel.loading.observe(requireActivity()) { loading ->
            if (loading) {
                loadingDialog.starLoading()
            } else {
                loadingDialog.isDismiss()
            }
        }

        //Cambiar el tipo letra de Ubuntu
        TypefaceUtil.asignarTipoLetra(
            requireContext(),
            null,
            TextView(
                requireContext()
            ),
            binding.perfilTxt,
            binding.infoTxt,
            binding.uidAdminTXT,
            binding.nombreAdminTXT,
            binding.apellidosAdminTXT,
            binding.correoAdminTXT,
            binding.uidAdmin,
            binding.nombreAdmin,
            binding.apellidosAdmin,
            binding.correoAdmin,
            binding.CambioPass,
            binding.btnEdit
        )


        return view
    }

    fun initComponent() {
        // Inicializa la instancia del LoadingDialog
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.mensaje = "Agregando valores por favor espere..."
        loadingDialog.setCancelable = false

    }

    fun eventos() {

        binding.CambioPass.setOnClickListener {
            startActivity(Intent(requireContext(), CambioPass::class.java))
        }

        binding.btnEdit.setOnClickListener {
            opcionesDialog()
        }

        binding.imagenADMIN.setOnClickListener {
            changeImageDialog()

        }
    }

    private fun opcionesDialog() {

        //Dialogo que muestra opciones para editar el perfil


        val opciones = arrayOf("Editar Nombre", "Editar Apellido")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Elegir Opcion")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        // "Selecionando: Editar Nombre"
                        editDialog("nombre")
                    }

                    1 -> {
                        // "Selecionando: Editar Apellido"
                        editDialog("apellido")
                    }
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .setCancelable(false)
            .create()

            .show()
    }

    private fun editDialog(valor: String) {


        //Crear la variable contenedores de los alerta, editText y layout
        val builder = AlertDialog.Builder(requireContext())
        val editText = android.widget.EditText(requireContext())
        val layout = LinearLayout(requireContext())

        //Crear el editText
        editText.hint = "Ingresa tu $valor ... "
        editText.isSingleLine = true
        editText.inputType =
            InputType.TYPE_TEXT_FLAG_CAP_WORDS or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT


        //Crear el layout para el dialogo
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 30, 30, 30)
        layout.addView(editText)

        //Asignar el layout al builder
        builder.setView(layout)
        builder.setTitle("Editar $valor")
        builder.setCancelable(false)

        builder.setPositiveButton("Guardar") { _, _ ->
            if (editText.text.isNotEmpty()) {
                val contenido = editText.text.toString()

                if (valor == "nombre") {
                    //Guardar el nombre
                    perfilAdminViewModel.editName(contenido)

                } else if (valor == "apellido") {
                    //Guardar el apellido
                    perfilAdminViewModel.editLastname(contenido)
                }
            } else {
                Toast.makeText(requireContext(), "Ingresa tu $valor", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { _, _ -> }
        builder.show()
    }

    private fun changeImageDialog() {
        val opciones = arrayOf("Galeria", "Camara")

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Elegir Opcion")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> {
                        selectImageFromGalery()
                    }

                    1 -> {
                        if (ContextCompat.checkSelfPermission( requireActivity(), Manifest.permission.CAMERA ) ==
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            openCamera()
                        } else {
                            SolicitudPermisoCamara.launch(Manifest.permission.CAMERA)
                        }


                    }
                }
            }
            .show()
    }

    private fun selectImageFromGalery() {
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
                rutaArchivoUri = data?.data!!

                // Mostrar la imagen en ImageView
                binding.imagenADMIN.setImageURI(rutaArchivoUri)

                // Obtener la extension de la ImagenView
                extensionImagen = obtenerExtension(rutaArchivoUri!!)

                // Actualizar el ViewModel
                perfilAdminViewModel.editImage(rutaArchivoUri.toString(), extensionImagen!!)

            } else {
                Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun obtenerExtension(rutaArchivoUri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(rutaArchivoUri))
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getImageCamara.launch(intent)
    }

    private val getImageCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.extras != null && data.extras!!.containsKey("data")) {
                val imageData = data.extras!!.get("data")
                if (imageData is Bitmap) {
                    // Guardar la imagen en el almacenamiento interno y cargarla como un Bitmap
                    val bitmap = saveImageToInternalStorage(imageData)

                    extensionImagen = "jpg"

                    // Pasar el Bitmap al ViewModel
                    perfilAdminViewModel.editImageCamara(bitmap, extensionImagen!!)
                } else {
                    Toast.makeText(requireContext(), "Error: No se pudo obtener la imagen de la cámara", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Error: No se pudo obtener la imagen de la cámara", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Bitmap {
        val wrapper = ContextWrapper(requireContext())
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "uniqueFileName.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Devolver el Bitmap para su uso posterior
        return BitmapFactory.decodeFile(file.absolutePath)
    }


    private val SolicitudPermisoCamara = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //
            openCamera()
        } else {
            Toast.makeText(activity, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

}


