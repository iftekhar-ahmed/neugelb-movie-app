package com.example.moviecatalog.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 01/07/2021.
 */
data class Movie(
    val id: String,
    val runtime: Int,
    val tagline: String,
    @SerializedName("original_title") val title: String,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    val genres: Array<Genre>
) {
    data class Genre(
        val id: String,
        val name: String
    )
}