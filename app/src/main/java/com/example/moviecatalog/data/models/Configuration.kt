package com.example.moviecatalog.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */
data class Configuration(
    val images: Images
) {
    data class Images(
        @SerializedName("base_url") val baseUrl: String,
        @SerializedName("poster_sizes") val posterSizes: Array<String>
    ) {
        companion object {
            const val POSTER_SIZE_W500 = "w500"
        }

        val posterSize
            get(): String {
                return if (posterSizes.contains(POSTER_SIZE_W500))
                    POSTER_SIZE_W500
                else posterSizes.last()
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Images

            if (baseUrl != other.baseUrl) return false
            if (!posterSizes.contentEquals(other.posterSizes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = baseUrl.hashCode()
            result = 31 * result + posterSizes.contentHashCode()
            return result
        }
    }
}