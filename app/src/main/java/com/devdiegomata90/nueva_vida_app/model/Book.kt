package com.devdiegomata90.nueva_vida_app.model

import com.devdiegomata90.nueva_vida_app.network.Chapter

data class Book(
    val codigo: String,
    val version_book: String,
    val name: String,
    val order: Int,
    val testament: String,
    val capitulos: List<Chapter>,
)