package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.domain.GetUserListUseCase
import com.devdiegomata90.nueva_vida_app.domain.SearchUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ListAdminViewModel : ViewModel() {

    //Livedata
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users get() = _users

    private val _name = MutableLiveData<String>()
    val name get() = _name

    //Casos de uso
    val getUserListUseCase = GetUserListUseCase()
    val searchUserUseCase = SearchUserUseCase()

    //Funciones
    fun onCreate() {
        users()
    }

    private fun users() {

        viewModelScope.launch {

            val result = getUserListUseCase()

            result.collect { _users.value = it }

            Log.d("ListAdminVM", "Datos $result")

        }
    }

    fun searchUser(name: String?) {

        viewModelScope.launch {

            val result = searchUserUseCase(name!!)

            result.collect { _users.value = it }

        }
    }


}