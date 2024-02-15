package com.devdiegomata90.nueva_vida_app.domain

import com.devdiegomata90.nueva_vida_app.data.repository.UserRepository

class ChangePassUseCase {
    //repository
    private val repository = UserRepository()

    suspend operator fun invoke(email:String,currentPass:String, newPass:String): Boolean =
         repository.changePass( email, currentPass,newPass)


}