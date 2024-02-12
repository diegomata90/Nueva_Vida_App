package com.devdiegomata90.nueva_vida_app.data.repository

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AudioRepository {

    private var referenciaBD = FirebaseDatabase.getInstance().getReference("AUDIOS")
    private val auth = FirebaseAuth.getInstance()

    private var rutaImageSubida: String = "Audio/Imagen_Audio/"
    private var rutaAudioSubida: String = "Audio/Audio_Subido/"
    private var storage = FirebaseStorage.getInstance()
    private var downloadUrl: Uri? =null
    private var downloadUrlAudio: Uri? = null


    suspend fun getAllAudioFlow(): Flow<List<Audio?>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Audio::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        referenciaBD.addValueEventListener(valueEventListener)

        // Cancelar el listener cuando se cierra el canal
        awaitClose { referenciaBD.removeEventListener(valueEventListener) }
    }

    suspend fun updateAudio(audio: Audio): Boolean {

        return try {

            /* -----------------------------
               ---AGREGAR AUDIO
               -------------------------------- */
            addAudioSound(audio)

            /* -----------------------------
               ---AGREGAR IMAGEN
               -------------------------------- */
            addAudioImagen(audio)

            /* -----------------------------
            ---AGREGAR DETALLE
            -------------------------------- */
            //Usuario que modifico el audio
            val uid = auth.currentUser?.uid

            //Obtener el nombre
            val name =
                FirebaseDatabase.getInstance().getReference("BD ADMINISTRADORES").child(uid!!)
                    .child("NOMBRES").get().await().value.toString()

            //Guarda el audio en la base de datos
            audio.uid = "$name - $uid - m"

            //Validar si downloadUrl existe, si existe se actualiza
            if (downloadUrl != null) {
                audio.imagen = downloadUrl.toString()
            }

            //Validar si downloadAudio existe, si existe se actualiza
            if (downloadUrlAudio != null) {
                audio.url = downloadUrlAudio.toString()
            }

            referenciaBD.child(audio.id.toString()).setValue(audio).await()
            true
        } catch (e: Exception) {
            Log.e("updateAudioR", e.toString())
            false
        }
    }

    //Funcion para agregar un audio
    suspend fun addAudioSound(audio: Audio): Boolean {

        //Validar URL audio es nulo,
        // Si true >> se inserta el audio,
        // Si false >> No hacer la insercion porque ya existe y Devuelve true

        return if (audio.url == null) {
            //Insertar el audio
            try {

                //Obtener la direcion uri
                val uri: Uri = Uri.parse(audio.audioUri)

                if (uri != null) {

                    //Obtener las extensiones
                    val extension = audio.extentionAudio.toString()
                    //val extension = audioUri.path?.split(".")

                    // Referencia al Storage
                    val storageRef_Audio = storage.reference

                    // Ruta en el Storage donde deseas almacenar la imagen
                    val path = "$rutaAudioSubida/audio_${System.currentTimeMillis()}.$extension"

                    // Referencia al archivo en el Storage
                    val audioRef = storageRef_Audio.child(path)

                    // Subir la imagen al Storage
                    val uploadTask = audioRef.putFile(uri).await()

                    // Obtener la URL de la imagen subida
                    downloadUrlAudio = uploadTask.storage.downloadUrl.await()

                    //Borrar el audioUri
                    audio.audioUri = null
                }

                true

            } catch (e: Exception) {
                Log.e("updateAudioAS", e.toString())
                false
            }

        } else {
            //No insertar el audio porque ya existe
            Log.i("updateAudioAS", "No insertar el audio porque ya existe")
            true

        }
    }

    //Funcion para agregar una imagen del audio
    suspend fun addAudioImagen(audio: Audio): Boolean {
        //Validar URL imagen es nulo,
        // Si true >> se inserta el imagen,
        // Si false >> No hacer la insercion porque ya existe y Devuelve true

        return if (audio.imagen == null) {
            //Insertar la imagen
            try {

                //Obtener la direcion uri
                val uri: Uri = Uri.parse(audio.imagenUri)


                if (uri != null) {

                    //Obtener las extensiones
                    val extension = audio.extentionAudio.toString()
                    //val extensiones = audioUri.path?.split(".")

                    // Referencia al Storage
                    val storageRef = storage.reference

                    // Ruta en el Storage donde deseas almacenar la imagen
                    val path =
                        "$rutaImageSubida/img_${System.currentTimeMillis()}.$extension"

                    // Referencia al archivo en el Storage
                    val imageRef = storageRef.child(path)

                    // Subir la imagen al Storage
                    val uploadTask = imageRef.putFile(uri).await()

                    // Obtener la URL de la imagen subida
                    downloadUrl = uploadTask.storage.downloadUrl.await()

                    //Borrar el imagenUri
                    audio.imagenUri = null
                }

                true

            } catch (e: Exception) {
                Log.e("updateAudioAI", e.toString())
                false
            }

        } else {
            //No insertar el imagen porque ya existe
            Log.i("updateAudioAI", "No insertar la imagen porque ya existe")
            true
        }
    }


}