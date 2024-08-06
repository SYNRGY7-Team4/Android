package com.synrgy7team4.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginLoginResponse(
    @SerializedName("data")
    val data: DataX,
    @SerializedName("success")
    val success: Boolean
)