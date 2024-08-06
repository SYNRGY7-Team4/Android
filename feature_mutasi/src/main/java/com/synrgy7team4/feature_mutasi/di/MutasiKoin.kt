package com.synrgy7team4.feature_mutasi.di

import com.synrgy7team4.feature_mutasi.data.Repository
import com.synrgy7team4.feature_mutasi.data.remote.RemoteDataSource

import org.koin.dsl.module

val MutasiKoin = module {


    factory {  RemoteDataSource(get()) }
    factory {  Repository(get()) }
}