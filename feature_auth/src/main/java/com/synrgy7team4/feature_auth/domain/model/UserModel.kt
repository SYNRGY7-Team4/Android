package com.synrgy7team4.feature_auth.domain.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val email: String,
    val id: String,
    val name: String,
    val token: String
)
