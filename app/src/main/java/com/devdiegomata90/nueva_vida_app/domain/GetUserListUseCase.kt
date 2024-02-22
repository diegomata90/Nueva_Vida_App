package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserListUseCase {
    private val repository = UserRepository()

    operator suspend fun invoke(): Flow<List<User>> = repository.userList()
}