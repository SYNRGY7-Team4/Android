package com.synrgy7team4.feature_auth.data.remote.request

data class LoginRequest(
    val email: String,
    val password: String,
)