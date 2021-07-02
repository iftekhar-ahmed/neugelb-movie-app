package com.example.moviecatalog.service

import com.google.gson.annotations.SerializedName

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */
data class ApiError(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val status: String
)