package com.devdiegomata90.nueva_vida_app.data.repository

import android.util.Log
import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VideoRepository {

    private val firebase = FirebaseDatabase.getInstance().getReference("VIDEOS")


    suspend fun getVideos(): Flow<List<Video>> = callbackFlow {

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Video::class.java) })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val query = firebase.orderByChild("TITULO")
        query.addValueEventListener(valueEventListener)

        Log.d("FVIDEOS", "VIDEOS: ${query}")
        awaitClose { query.removeEventListener(valueEventListener) }
    }

}