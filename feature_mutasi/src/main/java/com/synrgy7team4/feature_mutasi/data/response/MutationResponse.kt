package com.synrgy7team4.feature_mutasi.data.response

data class MutationResponse(
    val success: Boolean,
    val data: List<MutationData>,
    val message: String?,
    val errors: String?
)


