package com.synrgy7team4.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

//DataX is Response from Loginn
data class DataX(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("jwt_token")
    val jwtToken: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)