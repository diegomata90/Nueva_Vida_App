package com.devdiegomata90.nueva_vida_app.data.repository

import android.util.Log
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository {

    //Variables
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var firebase = FirebaseDatabase.getInstance().getReference("BD ADMINISTRADORES")
    private var user: User? = null

    suspend fun getUser(): User? {
        return try {

            //Validar si el usuario ha iniciado sesión
            if (currentUser != null) {
                val uid = currentUser.uid
                // Accede a los datos del usuario
                user = firebase.child(uid).get().await().getValue(User::class.java)

                Log.d("GETUSUARIO", "Datos capturadoras: $user")
                return user
            } else {
                // Manejar el caso en que el usuario no ha iniciado sesión
                Log.d("GETUSUARIO", "Usuario no autenticado.")
                return null
            }


        }catch(e: Exception) {
            Log.d("GETUSUARIO", "error al intentar mostrar el usuario: ${e.message}")
            null
        }


    }
}