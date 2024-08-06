package com.synrgy7team4.feature_auth.data.remote

import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.data.remote.retrofit.ApiService


class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getPosts() = apiService.getPosts()
}

//class RemoteDataSource(private val apiService: ApiService) {
//    suspend fun login(loginRequest: LoginRequest): LoginResponse =
//        apiService.login(loginRequest)
//}