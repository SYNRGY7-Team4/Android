package com.synrgy7team4.feature_auth.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.synrgy7team4.feature_auth.data.Repository
import com.synrgy7team4.feature_auth.data.local.AuthLocalSource
import com.synrgy7team4.feature_auth.data.local.ImplementLocalSource
import com.synrgy7team4.feature_auth.data.local.dataStore
import com.synrgy7team4.feature_auth.data.remote.AuthRemoteSource
import com.synrgy7team4.feature_auth.data.remote.ImplementAuthRemote
import com.synrgy7team4.feature_auth.data.remote.RemoteDataSource
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideConverterFactory
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideHttpClient
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideHttpLoggingInterceptor
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideRetrofit
import com.synrgy7team4.feature_auth.data.remote.retrofit.provideService
import com.synrgy7team4.feature_auth.data.repository.ImplementAuthRepository
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AuthKoin = module {
    single { provideHttpClient() }
    single { provideConverterFactory() }
    single { provideRetrofit(get()) }
    single { provideHttpLoggingInterceptor() }
    single { provideService() }

    single<AuthRepository> {ImplementAuthRepository(authRemoteSource = get(), authLocalSource = get())}
    single<AuthRemoteSource> {ImplementAuthRemote(get())}
    single<AuthLocalSource> { ImplementLocalSource(get()) }
    single<DataStore<Preferences>> { androidContext().dataStore }

    factory {  RemoteDataSource(get()) }
    factory {  Repository(get()) }
}