package com.synrgy7team4.feature_mutasi.presentation.viewmodel

import com.synrgy7team4.common.SharedPrefHelper
import com.synrgy7team4.feature_mutasi.data.Repository
import com.synrgy7team4.feature_mutasi.data.remote.ApiConfig
import com.synrgy7team4.feature_mutasi.data.remote.ApiService
import com.synrgy7team4.feature_mutasi.data.remote.RemoteDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MutasiViewModelKoin = module {

    // Provide SharedPrefHelper instance
    single { SharedPrefHelper(get()) }

    // Provide ApiService instance
    single<ApiService> {
        val sharedPrefHelper = get<SharedPrefHelper>()
        val token = sharedPrefHelper.getJwtToken() ?: ""
        ApiConfig.provideApiService(get(), token)
    }

    single { RemoteDataSource(get()) }

    // Provide the MutationRepository instance
    single { Repository(get()) }

    // Provide the MutationViewModel instance
    viewModel { MutasiViewmodel(get()) }
}


