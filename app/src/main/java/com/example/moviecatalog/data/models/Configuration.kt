package com.example.moviecatalog.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */

@Parcelize
data class Configuration(
    val images: Images
): Parcelable {
    @Parcelize
    data class Images(
        @SerializedName("base_url") val baseUrl: String,
        @SerializedName("backdrop_sizes") val backdropSizes: Array<String>,
        @SerializedName("poster_sizes") val posterSizes: Array<String>
    ): Parcelable {
        companion object {
            const val BACKDROP_SIZE_W780 = "w780"
            const val POSTER_SIZE_W500 = "w500"
        }

        val backdropSize
            get(): String {
                return if (backdropSizes.contains(BACKDROP_SIZE_W780))
                    BACKDROP_SIZE_W780
                else backdropSizes.last()
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