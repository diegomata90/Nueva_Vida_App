package com.devdiegomata90.nueva_vida_app.data.repository


import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow


class CategoryRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Categorias")

    suspend fun getAllCategoriesFlow(): Flow<List<Categoria?>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.getValue(Categoria::class.java) })
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


