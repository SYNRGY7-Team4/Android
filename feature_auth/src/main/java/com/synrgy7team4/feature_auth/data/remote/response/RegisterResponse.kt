package com.synrgy7team4.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)