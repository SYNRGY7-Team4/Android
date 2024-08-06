package com.synrgy7team4.feature_mutasi.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserData,
    @SerializedName("message") val message: String,
    @SerializedName("errors") val errors: String?
)

data class UserData(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("no_ktp") val noKtp: String,
    @SerializedName("no_hp") val noHp: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("account_pin") val accountPin: String,
    @SerializedName("ektp_photo") val ektpPhoto: String
)

