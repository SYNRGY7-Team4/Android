package com.synrgy7team4.feature_auth.data.remote

import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest

interface AuthRemoteSource {

//    suspend fun register(name: String, email: String, password: String): RegisterResponse
//    suspend fun register(registerBody: RegisterBody): Data
    suspend fun register(registerRequest: RegisterRequest): RegistResponse
//    suspend fun register(registerBody: RegisterBody, context: Context, uri: Uri): Data

    suspend fun login(loginRequest: LoginRequest): LoginResponse
//    suspend fun login(email: String, password: String): DataX

}