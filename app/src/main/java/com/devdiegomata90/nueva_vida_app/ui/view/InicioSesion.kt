package com.devdiegomata90.nueva_vida_app.ui.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.util.LoadingDialog
import com.google.firebase.auth.FirebaseAuth


class InicioSesion : AppCompatActivity() {

    lateinit var btnAceder:Button
    lateinit var Correo:EditText
    lateinit var Password:EditText
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var loadinDialog : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        //Se inicia el action bar
        actionBarpersonalizado("Inicio Sesión")

        // INICIALIZAR LAS VARIABLES
        Correo = findViewById(R.id.Correo)
        Password = findViewById(R.id.Password)
        btnAceder= findViewById(R.id.btnAcceder)

        //INICIALIZA FIREBASE
        firebaseAuth = FirebaseAuth.getInstance()

        //INICIALIZO PROGRESS DIALIGO PERSONALIZADO
        loadinDialog = LoadingDialog(this)
       // loadinDialog.setCancelable = true
        loadinDialog.mensaje = "Ingresando espere por favor"


        //Agrega evento al boton
        btnAceder.setOnClickListener {

            //Seteo de edit text a string
            val correoStr = Correo.text.toString()
            val passStr = Password.text.toString()
            
            //Validar los campos
            if(!Patterns.EMAIL_ADDRESS.matcher(correoStr).matches()){
                Correo.error = "Correo Inválido"
                Correo.isFocusable =true
                
            }else if(passStr.length<6){
                Password.error = "Contraseña Ingresada es menor igual a 6"
                Password.isFocusable=true
            }else{
                LogueAdministradores(correoStr,passStr)
            }
        }
    }

    private fun LogueAdministradores(correoStr: String, passStr: String) {
        loadinDialog.starLoading()
        firebaseAuth.signInWithEmailAndPassword(correoStr,passStr)

        firebaseAuth.signInWithEmailAndPassword(correoStr, passStr)
            .addOnCompleteListener(
                this@InicioSesion
            ) { task -> //si el inicio de sesion fue exitoso
                if (task.isSuccessful) {
                    loadinDialog.isDismiss()
                    val user = firebaseAuth.currentUser
                    startActivity(Intent(this@InicioSesion, MainActivityAdmin::class.java))
                    assert(user != null)
                    Toast.makeText(
                        this@InicioSesion,
                        "Bienvenido(a)" + user!!.email,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    loadinDialog.isDismiss()
                    UsuarioInvalido()
                }
            }.addOnFailureListener {
                loadinDialog.isDismiss()
                UsuarioInvalido()
            }

    }

    private fun UsuarioInvalido() {
        val builder = AlertDialog.Builder(this@InicioSesion)
        builder.setCancelable(false)
        builder.setTitle("!HA OCURRIDO UN ERRROR")
        builder.setMessage("Verifique si el correo o contraseña sean los correctos")
            .setPositiveButton(
                "Entendido"
            ) { dialog, which -> dialog.dismiss() }.show()
    }

    //Metodo para modificar el action bar
    private fun actionBarpersonalizado(titulo: String){
        // AFIRMAMOS QUE EL ACTIONBAR NO SEA NULO
        val actionBar = supportActionBar!!          // CREAMOS EL ACTIONBAR
        actionBar.title = titulo                   // LE ASINAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true) // HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}