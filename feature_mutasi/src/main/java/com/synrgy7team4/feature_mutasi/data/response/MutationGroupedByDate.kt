package com.synrgy7team4.feature_mutasi.data.response

import java.time.LocalDate

data class MutationGroupedByDate(
    val date: String,
    val mutations: List<MutationData>
)
