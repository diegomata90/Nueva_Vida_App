package com.devdiegomata90.nueva_vida_app.data.model

import com.google.gson.annotations.SerializedName

class Video {
    var ID: String? = null
    var TITULO: String? = null
    var DESCRIPCION: String? = null
    var LINK: String? = null
    var IMAGE: String? = null


    constructor()

    constructor(
        id: String?,
        titulo: String?,
        descripcion: String?,
        link: String?,
        image: String?
    ) {
        this.ID = id
        this.TITULO = titulo
        this.DESCRIPCION = descripcion
        this.LINK = link
        this.IMAGE = image
    }

}