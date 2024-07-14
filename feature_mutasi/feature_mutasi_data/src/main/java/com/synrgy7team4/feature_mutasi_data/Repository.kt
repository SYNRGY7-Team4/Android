package com.synrgy7team4.feature_mutasi_data

import com.synrgy7team4.feature_mutasi_data.remote.RemoteDataSource

class Repository(private val remoteDataSource: RemoteDataSource) {
    suspend fun getPostList() = remoteDataSource.getPosts()
}