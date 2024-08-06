package com.synrgy7team4.feature_mutasi.data.remote

import com.synrgy7team4.feature_mutasi.data.response.MutationResponse
import com.synrgy7team4.feature_mutasi.data.Post
import com.synrgy7team4.feature_mutasi.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("/api/transaction/mutations")
    suspend fun getMutationsByAcc(@Query("accountNumber") accountNumber: String): Response<MutationResponse>

    @GET("/api/user/me") // Replace with the correct endpoint
    suspend fun getUserData(): Response<UserResponse>


}