package com.devdiegomata90.nueva_vida_app.data.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("_id") val codigo: String,
    @SerializedName("id") val version_book: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("testament") val testament: String,
    @SerializedName("chapters") val capitulos: List<Chapter>
)

