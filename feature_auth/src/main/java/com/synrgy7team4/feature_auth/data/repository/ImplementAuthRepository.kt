package com.synrgy7team4.feature_auth.data.repository

import com.synrgy7team4.feature_auth.data.local.AuthLocalSource
import com.synrgy7team4.feature_auth.data.remote.AuthRemoteSource
import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.DataX
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ImplementAuthRepository(
    private val authLocalSource: AuthLocalSource,
    private val authRemoteSource: AuthRemoteSource
): AuthRepository {

//    override suspend fun register(registerBody: RegisterBody): Data {
//        return authRemoteSource.register(registerBody)
//    }

    override suspend fun register(registerRequest: RegisterRequest): RegistResponse {
        return authRemoteSource.register(registerRequest)
    }

//    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
//        return authRemoteSource.register(name, email, password)
//    }

//    override suspend fun register(registerBody: RegisterBody, context: Context, uri: Uri): Data {
//        return authRemoteSource.register(registerBody, context, uri)
//    }

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return authRemoteSource.login(loginRequest)
    }

    override suspend fun saveSession(dataX: DataX) {
        authLocalSource.saveSession(dataX)
    }

    override suspend fun loadSession(): Flow<DataX> {
        return authLocalSource.loadSession()
    }

    override suspend fun deleteSession() {
        authLocalSource.deleteSession()
    }

//    override suspend fun saveToken(token: String) {
//        authLocalSource.saveToken(token)
//
//    }
//
//    override suspend fun loadtoken(): String? {
//        return authLocalSource.loadtoken()
//    }
//
//    override suspend fun deleteToken() {
//        authLocalSource.deleteToken()
//    }
}