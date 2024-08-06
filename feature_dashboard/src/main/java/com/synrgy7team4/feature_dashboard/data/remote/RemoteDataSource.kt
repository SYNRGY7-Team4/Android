package com.synrgy7team4.feature_dashboard.data.remote

import com.synrgy7team4.feature_dashboard.data.remote.retrofit.ApiService


class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getPosts() = apiService.getPosts()
}