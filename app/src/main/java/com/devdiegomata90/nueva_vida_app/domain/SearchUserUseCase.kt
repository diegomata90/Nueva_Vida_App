package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchUserUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(name: String): Flow<List<User>> {

        val administradores = repository.userList()

        if (name.isNullOrBlank() == true) {
            return administradores
        } else {

            val userFind = administradores.map { users ->
                //Filtrar por nombre, apellido o correo
               users.filter {
                   user -> user.NOMBRES!!.contains(name, ignoreCase = true) ?: false ||
                   user.APELLIDOS!!.contains(name, ignoreCase = true) ?: false ||
                   user.CORREO!!.contains(name, ignoreCase = true) ?: false

               }

            }

            return userFind
        }
    }
}
