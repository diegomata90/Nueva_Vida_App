package com.devdiegomata90.nueva_vida_app.data.repository

import android.net.Uri

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class AddAudioRepository {

    private var rutaAudioSubido: String = "Audio/Audio_Subido/"
    private var rutaImageSubida: String = "Audio/Imagen_Audio/"
    private var rutaBaseDatos: String = "AUDIOS"
    private var RutaArchivoUri: Uri? = null
    private var referenciaBD = FirebaseDatabase.getInstance().getReference(rutaBaseDatos)
    private val auth = FirebaseAuth.getInstance()


    suspend fun addAudio(audio: Audio): Boolean {

        try {
            //Genera el id del audio
            val audioID = referenciaBD.push().key

            //Usuario que creo el audio
            val uid = auth.currentUser?.uid

            //Obtener el nombre
            val name= FirebaseDatabase.getInstance().getReference("BD ADMINISTRADORES").child(uid!!).child("NOMBRES").get().await().value.toString()

            //Guarda el audio en la base de datos
            audio.id = audioID
            audio.uid = "$name - $uid"
            referenciaBD.child(audioID!!).setValue(audio).await()
            return true

        } catch (e: Exception) {
            println("Error al intentar guardar el audio: ${e.message}")
            return false
        }
    }

}

