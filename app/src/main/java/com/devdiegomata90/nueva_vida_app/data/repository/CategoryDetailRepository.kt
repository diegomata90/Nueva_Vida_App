package com.devdiegomata90.nueva_vida_app.data.repository


import android.util.Log
import com.devdiegomata90.nueva_vida_app.data.model.CategoriaDetalle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


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

    suspend fun updateVistasOCategorie(id:String,cat:String) : Boolean {

        return try {

            //Obtener el valor de la vista
            val vista  = databaseReference.child(cat).child(id).child("vistas").get().await().value

            Log.i("DatosRecibidosOCATEGORIASS", "Categoria: $cat, ID: $id, Vistas: $vista")

            //Convierta a int
            var intVistas = 0

            if(vista != null) {
                intVistas = "$vista".toInt()
            }

            //Sumar 1 a la vista
            intVistas += 1

            //Actualizar el valor de la vista
            databaseReference.child(cat).child(id).child("vistas").setValue(intVistas).await()

            true

        } catch (e: Exception) {

            Log.e("ERROR REPO VISTAOCATEGORIASS", "ERROR al actualizar la vista: ${e.message}")
            false
        }

    }

}