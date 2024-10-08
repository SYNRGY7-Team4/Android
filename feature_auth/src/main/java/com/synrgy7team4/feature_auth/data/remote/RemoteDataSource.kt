package com.synrgy7team4.feature_auth.data.remote


class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getPosts() = apiService.getPosts()
}