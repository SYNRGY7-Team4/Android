package com.synrgy7team4.feature_dashboard.data.remote.response


import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("data")
    val data: Double,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)