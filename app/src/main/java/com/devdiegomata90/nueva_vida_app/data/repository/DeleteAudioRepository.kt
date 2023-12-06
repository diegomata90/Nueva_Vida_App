package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await



class DeleteAudioRepository () {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("AUDIOS")

    suspend fun deleteAudio(audio: Audio):Boolean {

        try{
            val IdAudio = audio.id.toString()
            val Imagen = FirebaseStorage.getInstance().getReferenceFromUrl(audio.imagen!!)
            val Audio = FirebaseStorage.getInstance().getReferenceFromUrl(audio.url!!)


            //Elimina los datos de audio
            databaseReference.child(IdAudio).removeValue().await()

            //Elimina la imagen del storage
            Imagen.delete().await()

            //Elimina la audio del storage
            Audio.delete().await()


            return true
        }catch(e:Exception){
            println("Error al intentar eliminar: ${e.message}")
            return false
        }
    }
}
