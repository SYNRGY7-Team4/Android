package com.synrgy7team4.feature_mutasi.data.response

data class MutationData(
    val id: String,
    val amount: Int,
    val datetime: String,
    val type: String,
    val status: String,
    val description: String,
    val account_from: String,
    val account_to: String
)

