package com.devdiegomata90.nueva_vida_app.data.network.response

import com.google.gson.annotations.SerializedName

data class DailyVerseResponse(
    @SerializedName("capitulo") val capitulo: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("id") val id: String,
    @SerializedName("libro") val libro: String,
    @SerializedName("texto") val texto: String,
    @SerializedName("versiculo") val versiculo: String
) {
    constructor() : this(
        "", "", "", "", "", ""
    )
}
