package com.synrgy7team4.feature_auth.data.remote

import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.DataX
import com.synrgy7team4.feature_auth.data.remote.retrofit.ApiService
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse

class ImplementAuthRemote (
    private val apiService: ApiService
) : AuthRemoteSource {



    override suspend fun register(registerRequest: RegisterRequest): RegistResponse {
        return apiService.register(registerRequest)
    }


    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }









}