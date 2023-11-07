package com.devdiegomata90.nueva_vida_app.retrofit2

import com.google.gson.annotations.SerializedName

data class VersiculoResponse(
    @SerializedName("_id") val codigo: String,
    @SerializedName("chapterId") val capituloId: String,
    @SerializedName("cleanText") val cleanText: String,
    @SerializedName("id") val id: String,
    @SerializedName("reference") val capitulo: String,
    @SerializedName("text") val htmlText: String,
)


