package com.devdiegomata90.nueva_vida_app.FragmentoAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devdiegomata90.nueva_vida_app.R

class RegistrarAdmin : Fragment() {

    private lateinit var fechaRegistrarAdmin: TextView
    private lateinit var Correo :EditText
    private lateinit var Password: EditText
    private lateinit var Nombre:EditText
    private lateinit var Apellidos:EditText
    private lateinit var btnRegistrar:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_registrar_admin, container, false)

        //Inicializar la variable con su respectivo campo en fragmento Registro
        fechaRegistrarAdmin = view.findViewById(R.id.fechaRegistro)
        Correo = view.findViewById(R.id.correo)
        Password = view.findViewById(R.id.password)
        Nombre = view.findViewById(R.id.nombre)
        Apellidos = view.findViewById(R.id.apellidos)
        btnRegistrar = view.findViewById(R.id.btnregistrar)

        //Seteo de variable
        val correo:String = Correo.text.toString()
        val pass:String = Password.text.toString()



        //Evento
        btnRegistrar?.setOnClickListener { RegistrarAdministradores(correo , pass ) }

        return view
    }

     fun RegistrarAdministradores(correo:String, pass:String) {
         // Tu lógica de registro aquí
         val msg = Toast.makeText(requireContext(), "Registro correo:  $correo  contraseña: $pass", Toast.LENGTH_SHORT).show()
     }
}