package com.devdiegomata90.nueva_vida_app.data.repository

import com.devdiegomata90.nueva_vida_app.data.model.Categoria
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class CategoryDetailRepository {


    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("OTRAS CATEGORIAS")

    suspend fun getAllCategoriesDetailFlow(cat:String): Flow<List<CategoriaDetalle?>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //trySend(snapshot.children.mapNotNull { it.getValue(CategoriaDetalle::class.java) })
                trySend(snapshot.child(cat).children.mapNotNull { it.getValue(CategoriaDetalle::class.java) })
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