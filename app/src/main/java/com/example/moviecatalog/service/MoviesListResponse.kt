package com.example.moviecatalog.service

import com.example.moviecatalog.data.models.MinimalMovie
import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 28/06/2021.
 */
data class MoviesListResponse(
    val page: Int = 1,
    val results: Array<MinimalMovie>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
) {

    val nextPage get() = page + 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoviesListResponse

        if (page != other.page) return false
        if (!results.contentEquals(other.results)) return false
        if (totalResults != other.totalResults) return false
        if (totalPages != other.totalPages) return false

        return true
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + results.contentHashCode()
        result = 31 * result + totalResults
        result = 31 * result + totalPages
        return result
    }
}