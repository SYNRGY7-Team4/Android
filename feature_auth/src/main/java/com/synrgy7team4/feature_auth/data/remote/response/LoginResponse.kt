package com.synrgy7team4.feature_auth.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData? = null,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null
)

data class LoginData(

    @field:SerializedName("refresh_token")
    val refreshToken: String? = null,

    @field:SerializedName("jwt_token")
    val jwtToken: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)