package com.devdiegomata90.nueva_vida_app.repository

import android.content.Context
import android.widget.Toast
import com.devdiegomata90.nueva_vida_app.api.BooksApiServe
import com.devdiegomata90.nueva_vida_app.extensions.Extensions.toBook
import com.devdiegomata90.nueva_vida_app.model.Book
import retrofit2.HttpException
import java.io.IOException


class BooksRepositoryImpl(private val apiService: BooksApiServe, private val context: Context) :
    BooksRepository {
    override suspend fun getBooks(url: String): List<Book> {
        // Lógica para obtener libros desde la red

        try {
            //Corutina para ejecutar en el hilo secundario
            val call = apiService.getBooks(url)

            if (call.isSuccessful) {
                //almacena la respuesta
                val booksResponseList = call.body() ?: emptyList()

                // Transforma los objetos BooksResponse a objetos Book utilizando la extension function
                val book = booksResponseList.map { it.toBook() }

                //ordena y devuelve lista de libros
                return book.sortedBy { it.order }

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



