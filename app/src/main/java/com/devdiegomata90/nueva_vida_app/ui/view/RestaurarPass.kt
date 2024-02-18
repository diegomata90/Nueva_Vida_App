package com.devdiegomata90.nueva_vida_app.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.core.LoadingDialog
import com.devdiegomata90.nueva_vida_app.core.TypefaceUtil
import com.devdiegomata90.nueva_vida_app.databinding.ActivityRestaurarPassBinding
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.RestaurarPassViewModel

class RestaurarPass : AppCompatActivity() {

    lateinit var binding: ActivityRestaurarPassBinding
    val restPassViewModel: RestaurarPassViewModel by viewModels()
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurarPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //Se agregar el actionBar personalizado
        actionBarpersonalizado("Restaurar Contraseña")

        initComponent()
        eventos()


        //Escuchar el mensage
        restPassViewModel.message.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        //Escuchar el loading
        restPassViewModel.loading.observe(this) { loading ->
            if (loading) {
                loadingDialog.starLoading()
            } else {
                loadingDialog.isDismiss()
            }
        }


        //Asignar tipo de letra
        TypefaceUtil.asignarTipoLetra(
            this, null, binding.RestaurarTxt,
            binding.btnSentEmail, binding.Correo
        )

    }

    private fun initComponent() {
       //Inicializar el loading
        loadingDialog = LoadingDialog(this)
        loadingDialog.mensaje = "Enviando correo, espere por favor"
        loadingDialog.setCancelable = false
    }

    private fun eventos() {

        binding.btnSentEmail.setOnClickListener {
            //Seteo Valores
            val email = binding.Correo.text.toString()

            //Validar que los campos no esten vacios
            if (email.isEmpty() || email.isBlank()) {
                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show()
            } else {
                //enviar correo
                restPassViewModel.sendEmail(email)
            }
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
        return true
    }


}