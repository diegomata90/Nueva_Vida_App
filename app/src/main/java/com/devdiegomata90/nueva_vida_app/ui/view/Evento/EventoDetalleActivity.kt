package com.devdiegomata90.nueva_vida_app.ui.view.Evento


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.EventoDetalleViewModel
import com.devdiegomata90.nueva_vida_app.databinding.ActivityEventoDetalleBinding
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventoDetalleActivity : AppCompatActivity() {

    lateinit var binding: ActivityEventoDetalleBinding
    private val eventoDetalleViewModel: EventoDetalleViewModel by viewModels()
    private lateinit var titulo: String
    private lateinit var descripcion: String
    private lateinit var fecha: String
    private lateinit var hora: String
    private lateinit var lugar: String
    private lateinit var imagen: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventoDetalleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Detalle Evento")

        //Recuperar informacion enviada por el intent
        getDataInten()

        initComponent()

        //Cambiar tipo de letra
        TypefaceUtil.asignarTipoLetra(this, null,
            binding.TituloEvento,binding.DescripcionEvento,binding.LugarEvento,binding.FechaEvento,binding.HoraEvento)

        //Inicializar eventos
        initEventos()

    }

    private fun initComponent() {
        binding.TituloEvento.text = titulo
        binding.DescripcionEvento.text = descripcion

        binding.LugarEvento.text = lugar

        // Dar formato a la fecha "Dom 05 de octubre del 2023"
        val fechaSinFormato = fecha
        val fechaFormateada = formatFecha(fechaSinFormato)
        binding.FechaEvento.text = fechaFormateada

        // Dar formato a la hora de "10:10 a.m"
        val horasinFormato = hora
        val horaFormateada = formatHora(horasinFormato)
        binding.HoraEvento.text = horaFormateada


        try {
            //Asignar la imagen en la image viewer
            Picasso.get().load(imagen).placeholder(R.drawable.image_ico).into(binding.ImagenEvento)
        } catch (e: Exception) {
            //Asignar una imagen opcional en el image viewer
            Picasso.get().load(R.drawable.image_ico).into(binding.ImagenEvento)
        }

    }

    private fun getDataInten() {

        //Recuperar informacion enviada por el intent
        titulo= intent.getStringExtra("tituloEvento").toString()
        descripcion = intent.getStringExtra("descripcionEvento").toString()
        fecha= intent.getStringExtra("fechaEvento").toString()
        lugar= intent.getStringExtra("lugarEvento").toString()
        imagen = intent.getStringExtra("imagenEvento").toString()
        hora = intent.getStringExtra("horaEvento").toString()

    }

    private fun initEventos(){
        binding.AddCalendar.setOnClickListener {
            //Agregar evento al calendario
            Toast.makeText(this, "Agregando evento al calendario", Toast.LENGTH_SHORT).show()
        }

        binding.Compartir.setOnClickListener {

            val mensage = "Evento: $titulo \n" +
                    "Fecha:${formatFecha(fecha)} \n" +
                    "Lugar: $lugar \n" +
                    "Hora: ${formatHora(hora)} \n" +
                    "${getString(R.string.app_name)} ${getString(R.string.linkdescargaApp)}"


            //Compartir evento
            eventoDetalleViewModel.shareImage(this  , binding.ImagenEvento,mensage)
        }

        /*
        binding.Compartir.setOnClickListener {
         val text = "$imagen \n $titulo  ${formatFecha(fecha)} $lugar ${formatHora(hora)}"

            // Crear un Intent
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            // Iniciar la actividad de compartir
            this.startActivity(Intent.createChooser(sendIntent, null))

        }
        */


    }

    private fun formatFecha(fechaSinFormato: String): String {
        // Dividir la fecha en sus componentes
        val partes = fechaSinFormato.split("-")
        if (partes.size != 3) {
            // Manejar un formato de fecha incorrecto (debería ser dd-mm-yyyy)
            return fechaSinFormato
        }

        val dia = partes[0]
        val mesNumerico = partes[1]
        val anio = partes[2]

        // Convertir el componente del mes a su representación textual en español
        val meses = arrayOf(
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        val mesTexto = if (mesNumerico.toIntOrNull() in 1..12) {
            meses[mesNumerico.toInt() - 1]
        } else {
            // Manejar un mes inválido
            return fechaSinFormato
        }

        // Convertir el componente del dia a su representación textual en español
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, anio.toInt())
        cal.set(Calendar.MONTH, mesNumerico.toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, dia.toInt())
        val numDiaSemana = cal.get(Calendar.DAY_OF_WEEK)


        val nombreDia = arrayOf(
            "Dom","Lun", "Mar", "Mie", "Jue", "Vie", "Sab"
        )
        val diaText = if (numDiaSemana in 1..7) {
            nombreDia[numDiaSemana - 1]
        } else {
            // Manejar un dia inválido
            return fechaSinFormato
        }

        // Formatear la fecha en el nuevo formato
        return "$diaText $dia de $mesTexto del $anio"

    }

    private fun formatHora(hora:String):String {

        // Parsear la hora en formato "HH:mm" a una instancia de Date
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return try {
            val parsedTime = inputFormat.parse(hora)
            val formattedTime = outputFormat.format(parsedTime)

            // retorna la hora formateada
            formattedTime.toString()

        } catch (e: ParseException) {
            // Manejar errores de formato de hora aquí
            hora
        }

    }

    private fun actionBarpersonalizado(titulo: String) {
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        supportActionBar?.let {
            // CREAMOS EL ACTIONBAR
            it.title = titulo
            // LE ASINAMOS UN TITULO
            it.setDisplayHomeAsUpEnabled(true)
            // HABILITAMOS EL BOTON DE RETROCESO
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}