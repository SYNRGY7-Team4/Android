package com.synrgy7team4.feature_auth.domain.repository

import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.DataX
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

//    suspend fun register(name: String, email: String, password: String): RegisterResponse
//    suspend fun register(registerBody: RegisterBody): Data
    suspend fun register(registerRequest: RegisterRequest): RegistResponse
//    suspend fun register(registerBody: RegisterBody, context: Context, uri: Uri): Data
    suspend fun login(loginRequest: LoginRequest): LoginResponse
//    suspend fun login(email: String, password: String): DataX
//    suspend fun saveSession(userModel: UserModel)
//    suspend fun saveSession(loginResponse: LoginResponse)
    suspend fun saveSession(dataX: DataX)
//    suspend fun loadSession(): Flow<UserModel>
//    suspend fun loadSession(): Flow<LoginResponse>
    suspend fun loadSession(): Flow<DataX>
    suspend fun deleteSession()


}