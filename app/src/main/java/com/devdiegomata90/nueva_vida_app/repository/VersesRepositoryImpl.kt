package com.devdiegomata90.nueva_vida_app.repository

import android.content.Context
import android.widget.Toast
import com.devdiegomata90.nueva_vida_app.api.VersesApiServe
import com.devdiegomata90.nueva_vida_app.model.Verse
import com.devdiegomata90.nueva_vida_app.extensions.Extensions.toVerse
import retrofit2.HttpException
import java.io.IOException

class VersesRepositoryImpl(private val apiService: VersesApiServe, private val context: Context) :
    VersesRepository {
    override suspend fun getVerses(url: String): List<Verse> {

        try {
            val call = apiService.getVerses(url)

            if (call.isSuccessful) {
                //almacena la respuesta
                val versesResponseList = call.body() ?: emptyList()

                // Transforma los objetos BooksResponse a objetos Verse utilizando la extension function
                val verses = versesResponseList.map { it.toVerse() }

                //ordena y devuelve lista de libros
                return verses.sortedBy { it.id }

            } else {
                handleHttpError(call.code())
                return emptyList()
            }

        } catch (e: IOException) {
            // Manejo de errores de red (IOException)
            handleNetworkError(e)
            return emptyList()
        } catch (e: HttpException) {
            // Manejo de errores de HTTP (HttpException)
            handleHttpError(e.code())
            return emptyList()
        } catch (exception: Exception) {
            // Manejo de otros errores inesperados
            handleUnexpectedError(exception)
            return emptyList()
        }


    }

    private fun handleUnexpectedError(exception: Exception) {
        // Manejar otros errores inesperados aquí
        showErrorToast("Error inesperado: ${exception.message}")
    }

    private fun handleHttpError(httpCode: Int) {
        // Manejar códigos de error HTTP aquí
        showErrorToast("Error HTTP: $httpCode")
    }

    private fun handleNetworkError(exception: IOException) {
        // Manejar errores de red aquí
        showErrorToast("Error de red: ${exception.message}")
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}