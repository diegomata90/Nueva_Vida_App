package com.devdiegomata90.nueva_vida_app.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await



class AddAudioRepository {

    private var referenciaBD = FirebaseDatabase.getInstance().getReference("AUDIOS")
    private val auth = FirebaseAuth.getInstance()

    private var rutaImageSubida: String = "Audio/Imagen_Audio/"
    private var rutaAudioSubida: String = "Audio/Audio_Subido/"
    private var storage = FirebaseStorage.getInstance()
    private lateinit var downloadUrl: Uri
    private lateinit var downloadUrlAudio: Uri


    suspend fun addAudio(audio: Audio, RutaArchivoUri:Uri?, RutaAudioUri:Uri, extensiones:Pair<String?,String?>): Boolean {


        return try {
            /* -----------------------------
                ---AGREGAR AUDIO
            -------------------------------- */
            if(RutaAudioUri != null){
                // Referencia al Storage
                val storageRef_Audio = storage.reference

                // Ruta en el Storage donde deseas almacenar la imagen
                val path = "$rutaAudioSubida/audio_${System.currentTimeMillis()}.${extensiones.first}"

                // Referencia al archivo en el Storage
                val audioRef = storageRef_Audio.child(path)

                // Subir la imagen al Storage
                val uploadTask = audioRef.putFile(RutaAudioUri).await()

                // Obtener la URL de la imagen subida
                downloadUrlAudio = uploadTask.storage.downloadUrl.await()
            }


            /* -----------------------------
            ---AGREGAR IMAGEN
            -------------------------------- */
            if(RutaArchivoUri != null){
                // Referencia al Storage
                val storageRef = storage.reference

                // Ruta en el Storage donde deseas almacenar la imagen
                val path = "$rutaImageSubida/img_${System.currentTimeMillis()}.${extensiones.second}"

                // Referencia al archivo en el Storage
                val imageRef = storageRef.child(path)

                // Subir la imagen al Storage
                val uploadTask = imageRef.putFile(RutaArchivoUri).await()

                // Obtener la URL de la imagen subida
                downloadUrl = uploadTask.storage.downloadUrl.await()
            }


            /* -----------------------------
            ---AGREGAR DETALLE
            -------------------------------- */

            //Genera el id del audio
            val audioID = referenciaBD.push().key

            //Usuario que creo el audio
            val uid = auth.currentUser?.uid

            //Obtener el nombre
            val name =
                FirebaseDatabase.getInstance().getReference("BD ADMINISTRADORES").child(uid!!)
                    .child("NOMBRES").get().await().value.toString()

            //Guarda el audio en la base de datos
            audio.id = audioID
            audio.uid = "$name - $uid"
            audio.imagen = if (downloadUrl.toString() != "") downloadUrl.toString() else ""
            audio.url = if (downloadUrlAudio.toString() != "") downloadUrlAudio.toString() else ""
            referenciaBD.child(audioID!!).setValue(audio).await()

            true

        } catch (e: Exception) {
            println("Error al intentar guardar el audio: ${e.message}")
            false
        }
    }

}

