package com.synrgy7team4.feature_mutasi.data

import com.synrgy7team4.feature_mutasi.data.remote.RemoteDataSource
import com.synrgy7team4.feature_mutasi.data.response.MutationResponse
import retrofit2.Response


class Repository(private val remoteDataSource: RemoteDataSource) {
    suspend fun getPostList() = remoteDataSource.getPosts()

    suspend fun getMutations(accountNumber: String, token:String): Response<MutationResponse> {
        return remoteDataSource.getMutationsByAcc(accountNumber, token)
    }

    suspend fun getUserData(token:String) = remoteDataSource.getUserData(token)
}