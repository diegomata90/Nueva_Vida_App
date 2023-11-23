package com.devdiegomata90.nueva_vida_app.data.network.response

import com.google.gson.annotations.SerializedName

data class BooksResponse(
    @SerializedName("_id") val codigo: String,
    @SerializedName("id") val version_book: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("testament") val testament: String,
    @SerializedName("chapters") val capitulos: List<Chapter>
) {

}

data class Chapter(
    @SerializedName("chapter") val n_chapter: Int,
    @SerializedName("id") val osis_star: String,
    @SerializedName("osis_end") val osis_end: String
)