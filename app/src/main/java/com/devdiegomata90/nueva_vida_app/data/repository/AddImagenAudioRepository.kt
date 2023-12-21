package com.devdiegomata90.nueva_vida_app.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AddImagenAudioRepository {

    private var rutaImageSubida: String = "Audio/Imagen_Audio/"
    private var storage = FirebaseStorage.getInstance()

    suspend fun addCover(RutaArchivoUri: Uri): Uri? {

        /* Referencia al Storage */
        val storageRef = storage.reference

        // Ruta en el Storage donde deseas almacenar la imagen
        val path = "$rutaImageSubida/img_${System.currentTimeMillis()}.png"


        // Referencia al archivo en el Storage
        val imageRef = storageRef.child(path)

        return try {
            // Subir la imagen al Storage
            val uploadTask = imageRef.putFile(RutaArchivoUri).await()

            // Obtener la URL de la imagen subida
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            downloadUrl

        } catch (e: Exception) {
            println("Error al subir la imagen: ${e.message}")
            null
        }

    }
}