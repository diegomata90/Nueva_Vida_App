package com.devdiegomata90.nueva_vida_app.data.model

data class Categoria(
    val id: String? = null,
    val nombre: String? = null,
    val imagen: String? = null
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this(null, null, null)


}

