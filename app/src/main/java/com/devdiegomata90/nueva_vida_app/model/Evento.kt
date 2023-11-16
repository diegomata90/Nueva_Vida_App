package com.devdiegomata90.nueva_vida_app.model

class Evento {
    var id:String? = null
    var titulo: String? = null
    var descripcion: String? = null
    var lugar: String? = null
    var fecha: String? = null
    var hora: String? = null
    var imagen: String? = null
    var uid: String? =null


    constructor()

    constructor(
        id: String?,
        titulo: String?,
        descripcion: String?,
        lugar: String?,
        fecha: String?,
        hora: String?,
        imagen: String?,
        uid: String?
    ) {
        this.id = id
        this.titulo = titulo
        this.descripcion = descripcion
        this.lugar = lugar
        this.fecha = fecha
        this.hora = hora
        this.imagen = imagen
        this.uid = uid
    }


}