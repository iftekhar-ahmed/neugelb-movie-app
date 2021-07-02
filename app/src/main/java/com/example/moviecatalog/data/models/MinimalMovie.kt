package com.example.moviecatalog.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 01/07/2021.
 */
data class MinimalMovie(
    @SerializedName("adult") val isAdult: Boolean,
    @SerializedName("id") val id: Int?,
    @SerializedName("original_title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    var fullPosterPath: String = "" // We will update this from configuration
)