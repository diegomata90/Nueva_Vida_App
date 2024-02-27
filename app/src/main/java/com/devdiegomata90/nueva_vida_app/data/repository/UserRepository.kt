package com.devdiegomata90.nueva_vida_app.data.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

class UserRepository {

    //Variables
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var firebase = FirebaseDatabase.getInstance().getReference("BD ADMINISTRADORES")
    private var storage = FirebaseStorage.getInstance().reference
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


        } catch (e: Exception) {
            Log.d("GETUSUARIO", "error al intentar mostrar el usuario: ${e.message}")
            null
        }


    }

    suspend fun changePass(email: String, currentPass: String, newPass: String): Boolean {
        //Validar si
        return try {
            //Iniciar sesion
            auth.signInWithEmailAndPassword(email, currentPass).await()
            Log.e("CAMBIARPASSr2", "Email: $email, Password: $currentPass")

            //Cambiar la contraseña
            if (currentUser != null) {
                currentUser.updatePassword(newPass).await()
            }

            //Actualizar BD
            firebase.child(currentUser!!.uid).child("PASSWORD").setValue(newPass)

            //Cierre de sesion
            auth.signOut()

            true
        } catch (e: Exception) {
            Log.e("CAMBIARPASSr", "Error al intentar cambiar la contraseña: ${e.message}")
            false
        }
    }

    suspend fun resetPass(email: String): Boolean {

        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("CAMBIARPASSr", "Error al intentar cambiar la contraseña: ${e.message}")
            false
        }

    }

    suspend fun userList(): Flow<List<User>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(User::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

        }

        val query = firebase.orderByChild("NOMBRE")
        query.addValueEventListener(valueEventListener)

        awaitClose { query.removeEventListener(valueEventListener) }

    }

    suspend fun updateName(name: String): Boolean {

        return try {
            firebase.child(currentUser!!.uid).child("NOMBRES").setValue(name).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateLastname(lastname: String): Boolean {

        return try {
            firebase.child(currentUser!!.uid).child("APELLIDOS").setValue(lastname).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateImage(imageUri: String,extension: String): Boolean {
        return try {

            var downloadUrl:Uri? = null
            var uri:Uri = Uri.parse(imageUri)

            if (uri != null) {

                val path = "Avatar/${currentUser!!.uid}.$extension"

                val uploadTask = storage.child(path).putFile(uri).await()

                downloadUrl = uploadTask.storage.downloadUrl.await()

            }
            if (downloadUrl != null){

                //Actualizar la BD de firebase
                firebase.child(currentUser!!.uid).child("IMAGE").setValue(downloadUrl.toString()).await()
                Log.d("UPDATEIMAGE", "Se actualizo la imagen imageUri $imageUri uri $uri")
                true
            }
            else {
                Log.d("UPDATEIMAGE", "No se pudo actualizar la imagen")
                false
            }
        } catch (e: Exception) {
            Log.e("UPDATEIMAGE", "Error al intentar actualizar la imagen: ${e.message}")
            false
        }
    }

    suspend fun updateImageCamara(bitmap: Bitmap, extension: String): Boolean {
        return try {
            val path = "Avatar/${currentUser!!.uid}.$extension"

            // Guardar el bitmap como un archivo temporal
            val tempFile = File.createTempFile("image", extension)
            val outputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Subir el archivo temporal a Firebase Storage
            val uploadTask = storage.child(path).putFile(Uri.fromFile(tempFile)).await()

            // Obtener la URL de descarga de Firebase Storage
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            // Actualizar la base de datos de Firebase con la URL de descarga
            firebase.child(currentUser!!.uid).child("IMAGE").setValue(downloadUrl.toString()).await()

            Log.d("UPDATEIMAGE", "Se actualizó la imagen correctamente")
            true
        } catch (e: Exception) {
            Log.e("UPDATEIMAGE", "Error al intentar actualizar la imagen: ${e.message}")
            false
        }
    }
}

