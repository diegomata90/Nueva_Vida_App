package com.devdiegomata90.nueva_vida_app.data.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("chapter") val n_chapter: Int,
    @SerializedName("id") val osis_star: String,
    @SerializedName("osis_end") val osis_end: String
)
