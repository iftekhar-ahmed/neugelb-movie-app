package com.example.moviecatalog.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 01/07/2021.
 */
data class Movie(
    val id: String,
    val runtime: Int,
    val tagline: String?,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("original_title") val title: String,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    val genres: Array<Genre>,
    var fullPosterPath: String = "", // We will update this from configuration
    var fullBackdropPath: String = ""
) {
    data class Genre(
        val id: String,
        val name: String
    )

    data class Runtime(
        val hour: Int,
        val minutes: Int
    )

    fun getRuntime(): Runtime {
        val hour = runtime / 60
        return Runtime(hour, runtime - (hour * 60))
    }

    fun getGenresCsv(): String {
        return StringBuilder().apply {
            genres.forEachIndexed { index, genre ->
                append(genre.name)
                if (index != genres.lastIndex) append(", ")
            }
        }.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (runtime != other.runtime) return false
        if (tagline != other.tagline) return false
        if (posterPath != other.posterPath) return false
        if (backdropPath != other.backdropPath) return false
        if (title != other.title) return false
        if (overview != other.overview) return false
        if (releaseDate != other.releaseDate) return false
        if (!genres.contentEquals(other.genres)) return false
        if (fullPosterPath != other.fullPosterPath) return false
        if (fullBackdropPath != other.fullBackdropPath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + runtime
        result = 31 * result + tagline.hashCode()
        result = 31 * result + posterPath.hashCode()
        result = 31 * result + backdropPath.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + genres.contentHashCode()
        result = 31 * result + fullPosterPath.hashCode()
        result = 31 * result + fullBackdropPath.hashCode()
        return result
    }
}