package com.synrgy7team4.feature_mutasi.data
data class MutationResponse(
    val success: Boolean,
    val data: List<MutationData>,
    val message: String?,
    val errors: String?
)


