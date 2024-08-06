package com.synrgy7team4.feature_auth.data

import com.synrgy7team4.feature_auth.data.remote.RemoteDataSource
import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse

class Repository(private val remoteDataSource: RemoteDataSource) {
    suspend fun login(loginRequest: LoginRequest): LoginResponse =
        remoteDataSource.login(loginRequest)
}