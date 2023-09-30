package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.devdiegomata90.nueva_vida_app.ui.view.MainActivityAdmin
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.util.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class RegistrarAdmin : Fragment() {

    private lateinit var fechaRegistrarAdmin: TextView
    private lateinit var Correo: EditText
    private lateinit var Password: EditText
    private lateinit var Nombre: EditText
    private lateinit var Apellidos: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_registrar_admin, container, false)

        //Inicializar la variable con su respectivo campo en fragmento Registro
        fechaRegistrarAdmin = view.findViewById(R.id.fechaRegistro)
        Correo = view.findViewById(R.id.correo)
        Password = view.findViewById(R.id.password)
        Nombre = view.findViewById(R.id.nombre)
        Apellidos = view.findViewById(R.id.apellidos)
        btnRegistrar = view.findViewById(R.id.btnregistrar)



        //Capturar la fecha
        val fechaactual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy")

        val Sfechaactual = fechaactual.format(formato).toString()

        //Asignacion de fecha al texView
        fechaRegistrarAdmin.text = Sfechaactual



        //Eventos
        btnRegistrar.setOnClickListener {
            //Seteo de campos
            val correo: String = Correo.text.toString()
            val password: String = Password.text.toString()

            if (ValidarCampos()) {
                RegistrarAdministradores(correo, password)
            }
        }

        //Inicializa el dialogo
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.mensaje = "Registrando, espere por favor"
        loadingDialog.setCancelable= false

        return view
    }


    //METODO PARA REGISTRAR ADMINISTRADORES
    @RequiresApi(Build.VERSION_CODES.O)
    fun RegistrarAdministradores(email: String, pass: String) {

        //Muestra el progressdialog
        loadingDialog.starLoading()


        //Inicializando Firebase Authentication
        auth = FirebaseAuth.getInstance()


        //crea el usuario en firebase
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                // Si el administrador fue creado correctamente
                if (task.isSuccessful) {
                 //  progressDialog.dismiss()
                    loadingDialog.isDismiss()
                    // afirmar de que el adminstrador no es nulo
                    val user = auth.currentUser!!

                    //Convertir a cadena los datos de los adminstradores
                    val UID = user.uid
                    val nombre: String = Nombre.text.toString()
                    val apellidos = Apellidos.text.toString()
                    val fechaRegistro = LocalDate.now().toString()


                    //HasMap sirve para enviar datos para ser recogido en otro lado
                    val Administradores = HashMap<Any, Any>()
                    Administradores["UID"] = UID
                    Administradores["CORREO"] = email
                    Administradores["PASSWORD"] = pass
                    Administradores["NOMBRES"] = nombre
                    Administradores["APELLIDOS"] = apellidos
                    Administradores["FECHAREGISTRO"] = fechaRegistro
                    Administradores["IMAGE"] = ""

                    //Inicializar FirebaseDatabase
                    val database = FirebaseDatabase.getInstance()
                    val reference = database.getReference("BD ADMINISTRADORES")
                    reference.child(UID).setValue(Administradores)
                    startActivity(Intent(activity, MainActivityAdmin::class.java))
                    Toast.makeText(activity, "Registro existoso", Toast.LENGTH_SHORT).show()
                    activity!!.finish()
                } else {
                    //progressDialog.dismiss()
                    loadingDialog.isDismiss()
                    Toast.makeText(activity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    activity,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun ValidarCampos(): Boolean {

        //Seteo de campos
        val correo: String = Correo.text.toString()
        val password: String = Password.text.toString()
        val nombre: String = Nombre.text.toString()
        val apellidos: String = Apellidos.text.toString()

        //Validar que los campos no esten vacios
        if (correo.isEmpty() || correo.isBlank() && password.isEmpty() || password.isBlank() && nombre.isEmpty() || nombre.isBlank() && apellidos.isEmpty() || apellidos.isBlank()) {
            Toast.makeText(requireContext(), "Campos Vacios", Toast.LENGTH_SHORT).show()
            return false
        } else {
            // Validar correo
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Correo.error = "Correo Inválido"
                Correo.isFocusable = true
                return false
            } else if (password.length < 6) {
                Password.error = "La Contraseña debe ser mayor de 6 caracteres "
                Password.isFocusable = true
                return false
            } else {
               // Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }


}


