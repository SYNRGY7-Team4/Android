package com.synrgy7team4.feature_auth.data.remote.request

data class RegisterRequest(
    val email: String,
    val no_hp: String,
    val password: String,
    val no_ktp: String,
    val name: String,
    val date_of_birth: String,
    val pin: String,
    val ektp_photo: String,
)
