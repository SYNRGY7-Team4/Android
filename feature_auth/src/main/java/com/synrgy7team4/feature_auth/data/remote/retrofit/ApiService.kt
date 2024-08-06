package com.synrgy7team4.feature_auth.data.remote.retrofit

import com.synrgy7team4.feature_auth.data.Post
import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

//    @Multipart
//    @POST("auth/register")
//    suspend fun register(
//        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
//        @Part photo: MultipartBody.Part
//    ): Data



//
    @POST("auth/register")
    suspend fun register(
       @Body registerRequest: RegisterRequest
    ): RegistResponse
//        Data
//            RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
//            LoginResponse




}