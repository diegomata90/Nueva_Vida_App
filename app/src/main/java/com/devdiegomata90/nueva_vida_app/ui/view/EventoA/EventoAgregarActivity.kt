package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Evento
import com.devdiegomata90.nueva_vida_app.util.DataPickerFragment
import com.devdiegomata90.nueva_vida_app.util.LoadingDialog
import com.devdiegomata90.nueva_vida_app.util.TimePickerFragment
import com.google.firebase.database.FirebaseDatabase

class EventoAgregarActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var tituloEvento: EditText
    private lateinit var descripcionEvento: EditText
    private lateinit var lugarEvento: EditText
    private lateinit var fechaEvento: EditText
    private lateinit var horaEvento: EditText
    private lateinit var imagenAgregarEvento: AppCompatImageView
    private lateinit var publicarEvento: Button
    private lateinit var eventoData: Evento


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_agregar)
        initComponent()
        initUI()

        // Crear una instancia de Evento y asignarla a eventoData
        eventoData = Evento() // Esto inicializa eventoData

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Evento")


        //Evento del boton publicar
        publicarEvento.setOnClickListener {

            //Seteo de campos
            eventoData.titulo = tituloEvento.text.toString()
            eventoData.descripcion = descripcionEvento.text.toString()
            eventoData.fecha = fechaEvento.text.toString()
            eventoData.hora = horaEvento.text.toString()
            eventoData.lugar = lugarEvento.text.toString()
            eventoData.imagen = "url"// pendiente implementar
            eventoData.uid = "xyz" // Asumiendo que UID debería ser una cadena vacía aquí

            if (etValidado()) {
                RegistrarEvento(eventoData)
            } else {
                Toast.makeText(this, "Revisar campos vacios", Toast.LENGTH_SHORT).show()
            }

        }

    }

    //Inicializa los elemento del xml
    private fun initComponent() {

        //Inicializa el dialogo
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Registrando, espere por favor"
        loadingDialog.setCancelable = false


        //Inicializar
        tituloEvento = findViewById(R.id.TituloEvento)
        descripcionEvento = findViewById(R.id.DescripcionEvento)
        lugarEvento = findViewById(R.id.LugarEvento)
        fechaEvento = findViewById(R.id.FechaEvento)
        horaEvento = findViewById(R.id.HoraEvento)
        imagenAgregarEvento = findViewById(R.id.imagenAgregarEvento)
        publicarEvento = findViewById(R.id.PublicarEvento)

    }

    //Agrega evento
    private fun initUI() {

        //Crear un evento
        fechaEvento.setOnClickListener { showDatePickerDialog() }
        horaEvento.setOnClickListener { showTimePickerDialog() }

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
    private fun RegistrarEvento(Evento: Evento) {
        //Muestra el progressdialog
        loadingDialog.starLoading()

        //Convertir a cadena los datos de los adminstradores
        val UID = "1"

        // Crear un objeto Evento
        val nuevoEvento = Evento(
            null,  // El ID se generará automáticamente
            Evento.titulo,
            Evento.descripcion,
            Evento.lugar,
            Evento.fecha,
            Evento.hora,
            Evento.imagen,
            Evento.uid
        )

        //Inicializar FirebaseDatabase
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("EVENTOS")

        // Genera un nuevo ID único para el evento
        val eventoID = reference.push().key

        reference.child(eventoID!!).setValue(nuevoEvento).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadingDialog.isDismiss()
                Toast.makeText(this, "Éxito al agregar el evento", Toast.LENGTH_SHORT).show()
            } else {
                loadingDialog.isDismiss()
                Toast.makeText(this, "Error al agregar el evento: " + task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            loadingDialog.isDismiss()
            Toast.makeText(this, "Error al agregar el evento: " + e.message, Toast.LENGTH_SHORT).show()
        }

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