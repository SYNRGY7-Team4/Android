package com.synrgy7team4.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

data class RegistResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)