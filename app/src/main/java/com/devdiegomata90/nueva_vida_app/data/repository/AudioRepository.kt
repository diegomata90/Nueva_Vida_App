package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AudioRepository {

    private val databaseReference: DatabaseReference =FirebaseDatabase.getInstance().getReference("AUDIOS")

    suspend fun getAllAudioFlow(): Flow<List<Audio?>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Audio::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        databaseReference.addValueEventListener(valueEventListener)

        // Cancelar el listener cuando se cierra el canal
        awaitClose { databaseReference.removeEventListener(valueEventListener) }
    }

}