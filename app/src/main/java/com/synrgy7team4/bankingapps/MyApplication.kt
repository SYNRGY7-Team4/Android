package com.synrgy7team4.bankingapps

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.jer.shared.ViewModelFactoryProvider
import com.synrgy7team4.feature_auth.di.AuthKoin

import com.synrgy7team4.feature_auth.di.Module

import com.synrgy7team4.feature_auth.presentation.viewmodel.AuthViewModelKoin
import com.synrgy7team4.feature_dashboard.di.DashboardKoin
import com.synrgy7team4.feature_dashboard.presentation.viewmodel.DashboardViewModelKoin
import com.synrgy7team4.feature_mutasi.di.MutasiKoin
import com.synrgy7team4.feature_mutasi.presentation.viewmodel.MutasiViewModelKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application(), ViewModelFactoryProvider {

    lateinit var module: Module
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()

        module = Module(this)
        viewModelFactory = ViewModelFactory(module)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(

                AuthKoin,
                AuthViewModelKoin,
                DashboardKoin,
                MutasiKoin,
                MutasiViewModelKoin,
                DashboardViewModelKoin
            )
        }
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory
    }
}