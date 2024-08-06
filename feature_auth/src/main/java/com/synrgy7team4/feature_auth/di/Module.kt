package com.synrgy7team4.feature_auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.synrgy7team4.feature_auth.data.local.AuthLocalSource
import com.synrgy7team4.feature_auth.data.local.ImplementLocalSource
import com.synrgy7team4.feature_auth.data.local.dataStore
import com.synrgy7team4.feature_auth.data.remote.AuthRemoteSource
import com.synrgy7team4.feature_auth.data.remote.ImplementAuthRemote
import com.synrgy7team4.feature_auth.data.remote.retrofit.ApiService
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideService
import com.synrgy7team4.feature_auth.data.repository.ImplementAuthRepository
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import com.synrgy7team4.feature_auth.domain.usecase.LoginUseCase

class Module(context: Context) {

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository = authRepository)
    }

    val authRepository: AuthRepository by lazy {
        ImplementAuthRepository(authLocalSource = authLocalSource, authRemoteSource = authRemoteSource)
    }

    val authLocalSource: AuthLocalSource by lazy {
        ImplementLocalSource(dataStore = dataStore)
    }

    val authRemoteSource: AuthRemoteSource by lazy {
        ImplementAuthRemote(apiService = apiService)
    }

    val apiService: ApiService by lazy {
        provideService()
    }

    val dataStore: DataStore<Preferences> by lazy {
        context.dataStore
    }





}