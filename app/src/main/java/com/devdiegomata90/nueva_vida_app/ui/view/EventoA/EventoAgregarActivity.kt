package com.devdiegomata90.nueva_vida_app.ui.view.EventoA

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.util.DataPickerFragment

class EventoAgregarActivity : AppCompatActivity() {

    private lateinit var fecha:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento_agregar)

        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Nuevo Evento")

       //Inicializar
        fecha= findViewById(R.id.fecha)

        //Crear un evento
        fecha.setOnClickListener{ showDatePickerDialog()}

    }

    //Metodo para mostar el calendario
    private fun showDatePickerDialog(){
        val datePicker = DataPickerFragment{ year, month, day -> onDataSelected(year, month, day)}
        datePicker.show(supportFragmentManager,"datePicker")
    }

    //
    private fun onDataSelected(year: Int, month:Int, day:Int) {
        //val mes= getNombreMes(month)

        //Los meses se indexan desde 0 hasta 11, donde 0 representa enero y 11 representa diciembre.
        val mes = month + 1

        fecha.setText("Has selecionado dia $day mes $mes  y año $year")
    }

    /* Función para obtener el nombre del mes a partir de su índice
    private fun getNombreMes(month: Int): String {
       val meses = arrayOf("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre")
       return meses[month]
    }*/

    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String){
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!!          // CREAMOS EL ACTIONBAR
        actionBar.title = titulo                   // LE ASINAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true)
    }


    //Regresar al actividad anterior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}