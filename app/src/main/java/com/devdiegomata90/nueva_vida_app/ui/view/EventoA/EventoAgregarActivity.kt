package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap.CompressFormat.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.devdiegomata90.nueva_vida_app.core.calendar.DataPickerFragment
import com.devdiegomata90.nueva_vida_app.core.LoadingDialog
import com.devdiegomata90.nueva_vida_app.core.notification.NotificationBuilder
import com.devdiegomata90.nueva_vida_app.core.calendar.TimePickerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.util.*



class EventoAgregarActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var tituloEvento: EditText
    private lateinit var descripcionEvento: EditText
    private lateinit var lugarEvento: EditText
    private lateinit var fechaEvento: EditText
    private lateinit var horaEvento: EditText
    private lateinit var imagenAgregarEvento: AppCompatImageView
    private lateinit var botonEvento: Button
    private lateinit var nuevoEvento: Evento
    private lateinit var actualizarEvento: Evento
    private lateinit var eventoTXT: TextView
    private lateinit var switchEnviarNotificacion: Switch


    private var rutaImageSubida: String = "Eventos_Subidos/"
    private var rutaBaseDatos: String = "EVENTOS"
    private var RutaArchivoUri: Uri? = null

    private lateinit var firebaseAuth: FirebaseAuth
    var currentUser: FirebaseUser? = null
    private lateinit var mStorageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_agregar)
        initComponent()
        initUI()
        eventos()

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Evento")

        //Recibir los datos del intent para modificar evento
        recibirEvento()


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
        botonEvento = findViewById(R.id.BotonEvento)
        eventoTXT = findViewById(R.id.EventoTXT)
        switchEnviarNotificacion = findViewById(R.id.switchEnviarNotificacion)

    }

    //Init
    private fun initUI() {
        //Inicializa el dialogo
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Registrando, espere por favor"
        loadingDialog.setCancelable = false
    }

    //Agrega evento
    private fun eventos() {

        //Crear un evento
        fechaEvento.setOnClickListener { showDatePickerDialog() }
        horaEvento.setOnClickListener { showTimePickerDialog() }

        //Agregar evento para seleccionar la imagen
        imagenAgregarEvento.setOnClickListener {
            seleccionarImagenDeGaleria()
        }

        //Evento del boton publicar
        botonEvento.setOnClickListener {

            //Validar los campos
            if (etValidado()) {
                //Revisar si el boton es para actualizar o para publicar nuevo evento
                if (botonEvento.text == "Publicar") {
                    RegistrarEvento()
                } else {
                    iniciarActualizarEvento()
                }
            } else {
                Toast.makeText(this, "Revisar campos vacios", Toast.LENGTH_SHORT).show()
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

        // Formatear la fecha en el formato deseado "dd-MM-yyyy"
        val formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year)

        // Establecer la fecha formateada en el EditText
        fechaEvento.setText(formattedDate)
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

        // Parsear la hora en formato "HH:mm" a una instancia de Date
        //val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        //val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        try {
            //val parsedTime = inputFormat.parse(time)
            //val formattedTime = outputFormat.format(parsedTime)

            // Establecer la hora formateada en el EditText
            horaEvento.setText(time)

        } catch (e: ParseException) {
            // Manejar errores de formato de hora aquí
            Toast.makeText(this, "Error al formatear la hora error-->$e", Toast.LENGTH_SHORT).show()
        }

    }

    //Validacion de los campos
    private fun etValidado(): Boolean {

        //Seteo de valores
        val tituloEvento = tituloEvento.text.toString()
        val descripcionEvento = descripcionEvento.text.toString()
        val lugarEvento = lugarEvento.text.toString()
        val fechaEvento = fechaEvento.text.toString()
        val horaEvento = horaEvento.text.toString()

        return if (
            lugarEvento.isEmpty() || lugarEvento.isBlank() ||
            tituloEvento.isEmpty() || tituloEvento.isBlank() ||
            descripcionEvento.isEmpty() || descripcionEvento.isBlank() ||
            fechaEvento.isEmpty() || fechaEvento.isBlank() ||
            horaEvento.isEmpty() || horaEvento.isBlank()

        ) {
            Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }


    //Metodo para registrar evento
    private fun RegistrarEvento() {

        // Obtener el estado del Switch
        val enviarNotificacion = switchEnviarNotificacion.isChecked

        //INICIALIZAR INSTANCIA BASEDATOS
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser

        if (RutaArchivoUri == null) {
            Toast.makeText(this, "Seleciona una imagen para el Evento", Toast.LENGTH_SHORT).show()
        } else {
            //Muestra el progressdialog
            loadingDialog.starLoading()
            //Se sube la imagen al StorageFirebase

            val storageReference = FirebaseStorage.getInstance().reference.child(
                rutaImageSubida + "/im_" + System.currentTimeMillis() + "." + obtenerExtension(
                    RutaArchivoUri!!
                )
            )


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
                    nuevoEvento.id = eventoID.toString()
                    nuevoEvento.titulo = tituloEvento.text.toString()
                    nuevoEvento.descripcion = descripcionEvento.text.toString()
                    nuevoEvento.fecha = fechaEvento.text.toString()
                    nuevoEvento.hora = horaEvento.text.toString()
                    nuevoEvento.lugar = lugarEvento.text.toString()
                    nuevoEvento.imagen = downloadUrl
                    nuevoEvento.uid = currentUser?.email



                    reference.child(eventoID!!).setValue(nuevoEvento)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadingDialog.isDismiss()
                                Toast.makeText(
                                    this,
                                    "Éxito al agregar el evento",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Verifica si la casilla de notificación está marcada
                                if (switchEnviarNotificacion.isChecked) {
                                    envioNotificacion(nuevoEvento.titulo.toString(),nuevoEvento.descripcion.toString())
                                }
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
                        Toast.makeText(
                            this,
                            "Error al agregar el evento: " + e.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }.addOnFailureListener { e ->
                loadingDialog.isDismiss()
                Toast.makeText(this, "Error al agregar la imagen: " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }


        }
    }

    private fun obtenerExtension(rutaArchivoUri: Uri): Any? {

        // Obtener el nombre del archivo
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(RutaArchivoUri!!))
        return extension
    }

    //Metodo para recibir y setear datos recibidos del intent para actualizar eventos
    private fun recibirEvento() {
        //Se recibir los valores que viene de Viewholder para parsearlos al evento
        //RECUPERAR DATOS RECIBIDOS
        val intent = intent.extras

        //se inicializar el contenedor de los campos del intent a recibir
        actualizarEvento = Evento()

        if (intent != null) {

            //Se cambia el titulo del Actionbar personalizado
            supportActionBar!!.title = "Actualizar Evento"

            //Se cambiar el titulo de la activity
            eventoTXT.text = "Actualizar Evento"

            //Cambiar el nombre al boton
            botonEvento.text = "Actualizar"


            //Cargar datos en el contenedor de los campos recibido del intent
            actualizarEvento.id = intent.getString("eventoId");
            actualizarEvento.titulo = intent.getString("eventoTitulo");
            actualizarEvento.descripcion = intent.getString("eventoDescrip");
            actualizarEvento.fecha = intent.getString("eventoFecha")
            actualizarEvento.lugar = intent.getString("eventoLugar");
            actualizarEvento.hora = intent.getString("eventoHora");
            actualizarEvento.imagen = intent.getString("eventoImagen");

            //Seteo de valores recibidos en los EditText
            tituloEvento.setText(actualizarEvento.titulo)
            descripcionEvento.setText(actualizarEvento.descripcion)
            fechaEvento.setText(actualizarEvento.fecha)
            lugarEvento.setText(actualizarEvento.lugar)
            horaEvento.setText(actualizarEvento.hora)

            //Seteo de imagen
            try {
                Picasso.get().load(actualizarEvento.imagen).into(imagenAgregarEvento)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar la imagen: " + e.message, Toast.LENGTH_SHORT)
                    .show()
                Picasso.get().load(R.drawable.categoria).into(imagenAgregarEvento)
            }


        }
    }

    //Metodo para actualizar evento
    private fun iniciarActualizarEvento() {

        loadingDialog.starLoading()
        eliminarImagenAnterior()

    }

    private fun eliminarImagenAnterior() {

        val imagen = FirebaseStorage.getInstance().getReferenceFromUrl(actualizarEvento.imagen!!)

        imagen.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show()
                subirNuevaImagen()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar imagen:${e.message}", Toast.LENGTH_SHORT)
                    .show()
                loadingDialog.isDismiss()
            }


    }

    private fun subirNuevaImagen() {
        //Declaramos la nueva imagagen
        val nuevaImagen = "im_" + System.currentTimeMillis().toString() + ".png"

        //Referencia de almacenamiento, para que la nueva imagen se pueda guardar en esa carpeta
        val imageStorage =
            FirebaseStorage.getInstance().reference.child(rutaImageSubida + nuevaImagen)

        //Obtener mapa de bits de la nueva imagen seleccionada
        val bitmapStorage = (imagenAgregarEvento.drawable as BitmapDrawable).bitmap

        //Convetir bitmap a byte array
        val byteArray = ByteArrayOutputStream()
        //comprimir imagen
        bitmapStorage.compress(
            PNG,
            100,
            byteArray
        ) // Convierte el bitmap a un byte array or del byteArray

        //Se almacena la imagaen en una variable
        val data = byteArray.toByteArray()

        //Se almacena la imagen en el storage
        imageStorage.putBytes(data).addOnSuccessListener {}

        val uploadTask: UploadTask = imageStorage.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this@EventoAgregarActivity, "Nueva Imagen Cargada", Toast.LENGTH_SHORT)
                .show()
            //obtener la URL de la imagen recien cargada
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result
            //actualizar la base de datos con nuevos datos
            ActualizarImagenBD(downloadUri.toString())
        }.addOnFailureListener { e ->
            Toast.makeText(
                this@EventoAgregarActivity,
                "Error al subir la imagen: " + e.message,
                Toast.LENGTH_SHORT
            ).show()
            loadingDialog.isDismiss()
        }


    }

    private fun ActualizarImagenBD(Uri: String) {
        // Capturar los datos del evento y guardarlos en la base datos
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(rutaBaseDatos)

        //Setear los valores de los campos en la clase Evento
        val eventoTxt = Evento()
        eventoTxt.id = actualizarEvento.id
        eventoTxt.titulo = tituloEvento.text.toString()
        eventoTxt.descripcion = descripcionEvento.text.toString()
        eventoTxt.fecha = fechaEvento.text.toString()
        eventoTxt.hora = horaEvento.text.toString()
        eventoTxt.lugar = lugarEvento.text.toString()
        eventoTxt.uid = currentUser?.email

        //Consuta query
        val query = reference.orderByChild("id").equalTo(eventoTxt.id)

        //Seteo de datas en la base datos
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    ds.ref.child("titulo").setValue(eventoTxt.titulo)
                    ds.ref.child("descripcion").setValue(eventoTxt.descripcion)
                    ds.ref.child("fecha").setValue(eventoTxt.fecha)
                    ds.ref.child("hora").setValue(eventoTxt.hora)
                    ds.ref.child("lugar").setValue(eventoTxt.lugar)
                    ds.ref.child("uid").setValue(eventoTxt.uid)
                    ds.ref.child("imagen").setValue(Uri)
                }
                loadingDialog.isDismiss()
                Toast.makeText(this@EventoAgregarActivity, "Evento actualizado", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@EventoAgregarActivity, EventoaActivity::class.java))
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    //Metodo para la validad si envio o notificacion
    private fun envioNotificacion(title: String, message: String) {

        // Crea un objeto NotificationBuilder
        val notificationBuilder = NotificationBuilder(this, "my_channel")

        // Crea una notificación
        val notification = notificationBuilder.createNotification(
            title = title,
            message = message,
            smallIcon = R.drawable.info_ico,
            action = PendingIntent.getActivity(this, 0, Intent(this, EventoActivity::class.java), 0)
        )

        // Envia la notificación
        notificationBuilder.notify(notification)
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





