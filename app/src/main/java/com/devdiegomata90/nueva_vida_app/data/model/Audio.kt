package com.devdiegomata90.nueva_vida_app.data.model

import android.graphics.Bitmap

class Audio {
    var id: String? = null
    var titulo: String? = null
    var imagen: String? = null
    var url: String? = null
    var fecha: String? = null
    var uid: String? = null
    var descripcion: String? = null

    constructor()

    constructor(
        id: String?,
        titulo: String?,
        imagen: String?,
        url: String?,
        fecha: String?,
        uid: String?,
        descripcion: String?
    ) {
        this.id = id
        this.titulo = titulo
        this.imagen = imagen
        this.url = url
        this.fecha = fecha
        this.uid = uid
        this.descripcion = descripcion
    }

}
