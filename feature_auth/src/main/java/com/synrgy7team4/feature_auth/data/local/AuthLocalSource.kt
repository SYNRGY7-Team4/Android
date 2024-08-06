package com.synrgy7team4.feature_auth.data.local

import com.synrgy7team4.feature_auth.data.remote.response.DataX
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthLocalSource {

//    suspend fun saveToken(token: String)
//    suspend fun saveSession(userModel: UserModel)
//    suspend fun saveSession(loginResponse: LoginResponse)
    suspend fun saveSession(dataX: DataX)
//    suspend fun loadSession(): Flow<UserModel>
//    suspend fun loadSession(): Flow<LoginResponse>
    suspend fun loadSession(): Flow<DataX>
    suspend fun deleteSession()

}