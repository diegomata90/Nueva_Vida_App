package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.devdiegomata90.nueva_vida_app.databinding.ActivityCambioPassBinding
import com.devdiegomata90.nueva_vida_app.ui.view.MainActivityAdmin
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.CambioPassViewModel

class CambioPass : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityCambioPassBinding

    //viewModel
    private val cambioPassViewModel: CambioPassViewModel by viewModels()
    private lateinit var passActualET: String
    private lateinit var passNewET: String
    private lateinit var passConfirmET: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambioPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        eventoClick()

    }

    private fun eventoClick() {

        binding.IRINICIOBTN.setOnClickListener() {
            startActivity(Intent(this, MainActivityAdmin::class.java))
        }

        binding.CAMBIARPASSBTN.setOnClickListener() {

            // Seteo de valores
            binding.run {
                passActualET = PassActualET.text.toString()
                passNewET = PassNewET.text.toString()
                passConfirmET = PassConfirmET.text.toString()
            }

            // Validar los datos
            //Validar que los campos no esten vacios
            if (passActualET.isEmpty() || passNewET.isEmpty() || passConfirmET.isEmpty()) {
                Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
            }
            //Validar que las contraseñas coincidan
            else if (passNewET == passConfirmET) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
            //Validar que los campos sean mayores a 6 caracteres
            else if (passNewET.length < 6 || passConfirmET.length < 6) {
                Toast.makeText(this, "La contraseña debe ser mayor a 6 caracteres", Toast.LENGTH_SHORT)
                    .show()
            }
            //Cambiar el password
            else {
                cambioPassViewModel.changePass(
                    passActualET,
                    passNewET,
                    passConfirmET
                )
            }


        }
    }


}