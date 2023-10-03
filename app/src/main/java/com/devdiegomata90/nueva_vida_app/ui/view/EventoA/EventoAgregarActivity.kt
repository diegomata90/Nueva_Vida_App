package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.util.DataPickerFragment
import com.devdiegomata90.nueva_vida_app.util.LoadingDialog
import com.devdiegomata90.nueva_vida_app.util.TimePickerFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class EventoAgregarActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var tituloEvento: EditText
    private lateinit var descripcionEvento: EditText
    private lateinit var lugarEvento: EditText
    private lateinit var fechaEvento: EditText
    private lateinit var horaEvento: EditText
    private lateinit var imagenAgregarEvento: AppCompatImageView
    private lateinit var publicarEvento: Button
    private lateinit var nuevoEvento: Evento
    private lateinit var mStorageReference: StorageReference


    private var rutaSubida: String = "Eventos_Subidos"
    private var rutaBaseDatos: String = "EVENTOS"
    private var RutaArchivoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_agregar)
        initComponent()
        initUI()
        evento()


        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Evento")


        //Evento del boton publicar
        publicarEvento.setOnClickListener {

            if (etValidado()) {
                RegistrarEvento()
            } else {
                Toast.makeText(this, "Revisar campos vacios", Toast.LENGTH_SHORT).show()
            }

        }

    }

    //Inicializa los elemento del xml
    private fun initComponent() {

        //Inicializar
        tituloEvento = findViewById(R.id.TituloEvento)
        descripcionEvento = findViewById(R.id.DescripcionEvento)
        lugarEvento = findViewById(R.id.LugarEvento)
        fechaEvento = findViewById(R.id.FechaEvento)
        horaEvento = findViewById(R.id.HoraEvento)
        imagenAgregarEvento = findViewById(R.id.imagenAgregarEvento)
        publicarEvento = findViewById(R.id.PublicarEvento)

    }

    //Init
    private fun initUI() {
        //Inicializa el dialogo
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Registrando, espere por favor"
        loadingDialog.setCancelable = false
    }

    //Agrega evento
    private fun evento() {

        //Crear un evento
        fechaEvento.setOnClickListener { showDatePickerDialog() }
        horaEvento.setOnClickListener { showTimePickerDialog() }

        //Agregar evento para seleccionar la imagen
        imagenAgregarEvento.setOnClickListener {
            seleccionarImagenDeGaleria()
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
                RutaArchivoUri = data?.data
                // Mostrar la imagen en ImageView
                imagenAgregarEvento.setImageURI(RutaArchivoUri)
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }


    //Instancia de la clase DataPickerFragment
    private fun showDatePickerDialog() {
        val datePicker = DataPickerFragment { year, month, day -> onDataSelected(year, month, day) }
        datePicker.show(supportFragmentManager, "datePicker")
    }


    //Metodo Setea la fecha seleccionada en el editext
    private fun onDataSelected(year: Int, month: Int, day: Int) {
        val mes = getNombreMes(month)

        //Los meses se indexan desde 0 hasta 11, donde 0 representa enero y 11 representa diciembre.
        val month = month + 1

        fechaEvento.setText("$day de $mes del $year")
    }


    //Función para obtener el nombre del mes a partir de su índice
    private fun getNombreMes(month: Int): String {
        val meses = arrayOf(
            "enero",
            "febrero",
            "marzo",
            "abril",
            "mayo",
            "junio",
            "julio",
            "agosto",
            "septiembre",
            "octubre",
            "noviembre",
            "diciembre"
        )
        return meses[month]
    }


    //Instancia de la clase TimePickerFragment
    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelect(it) }
        timePicker.show(supportFragmentManager, "timePicker")
    }


    //Setea la hora seleccionada en el editext
    private fun onTimeSelect(time: String) {
        horaEvento.setText("$time")
    }

    //Validacion de los campos
    private fun etValidado(): Boolean {

        //Seteo de valores
        val _tituloEvento = tituloEvento.text.toString()
        val _descripcionEvento = descripcionEvento.text.toString()
        val _lugarEvento = lugarEvento.text.toString()
        val _fechaEvento = fechaEvento.text.toString()
        val _horaEvento = horaEvento.text.toString()

        return if (
            _lugarEvento.isEmpty() || _lugarEvento.isBlank() ||
            _tituloEvento.isEmpty() || _tituloEvento.isBlank() ||
            _descripcionEvento.isEmpty() || _descripcionEvento.isBlank() ||
            _fechaEvento.isEmpty() || _fechaEvento.isBlank() ||
            _horaEvento.isEmpty() || _horaEvento.isBlank()

        ) {
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }


    //Metodo para registrar evento
    private fun RegistrarEvento() {

        val titulo = tituloEvento.text.toString()

        if (titulo.isEmpty() || RutaArchivoUri == null) {
            Toast.makeText(this, "LLenar todos los campos ", Toast.LENGTH_SHORT).show()
        } else {
            //Muestra el progressdialog
            loadingDialog.starLoading()
            //Se sube la imagen al StorageFirebase

            val storageReference = FirebaseStorage.getInstance().reference.child(rutaSubida + "/imagen_" + System.currentTimeMillis())

            //Se guarda la imagen en el storege de firebase
            storageReference.putFile(RutaArchivoUri!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    val downloadUrl = downloadUri.toString()
                    // Aquí puedes hacer lo que necesites con la URL de descarga

                    //Inicializar FirebaseDatabase
                    val database = FirebaseDatabase.getInstance()
                    val reference = database.getReference(rutaBaseDatos)

                    // Genera un nuevo ID único para el evento
                    val eventoID = reference.push().key

                    // Crear una instancia de Evento y asignarla a eventoData
                    nuevoEvento = Evento() // Esto inicializa eventoData

                    //Seteo de campos
                    nuevoEvento.titulo = tituloEvento.text.toString()
                    nuevoEvento.descripcion = descripcionEvento.text.toString()
                    nuevoEvento.fecha = fechaEvento.text.toString()
                    nuevoEvento.hora = horaEvento.text.toString()
                    nuevoEvento.lugar = lugarEvento.text.toString()
                    nuevoEvento.imagen = downloadUrl
                    nuevoEvento.uid = "xyz" // Asumiendo que UID debería ser una cadena vacía aquí


                    reference.child(eventoID!!).setValue(nuevoEvento).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadingDialog.isDismiss()
                            Toast.makeText(this, "Éxito al agregar el evento", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, EventoaActivity::class.java))
                            finish()
                        } else {
                            loadingDialog.isDismiss()
                            Toast.makeText(
                                this,
                                "Error al agregar el evento: " + task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener { e ->
                        loadingDialog.isDismiss()
                        Toast.makeText(this, "Error al agregar el evento: " + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }.addOnFailureListener { e ->
                loadingDialog.isDismiss()
                Toast.makeText(this, "Error al agregar la imagen: " + e.message, Toast.LENGTH_SHORT).show()
            }


    }}


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