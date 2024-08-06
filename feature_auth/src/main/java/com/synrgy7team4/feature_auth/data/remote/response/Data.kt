package com.synrgy7team4.feature_auth.data.remote.response


import com.google.gson.annotations.SerializedName

//Data is response from Registerr
data class Data(
//    @SerializedName("id")
//    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("no_ktp")
    val noKtp: String,
    @SerializedName("no_hp")
    val noHp: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("account_pin")
    val accountPin: String,
    @SerializedName("ektp_photo")
    val ektpPhoto: String,
)