package com.synrgy7team4.feature_auth.domain.usecase

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginUseCase( private val authRepository: AuthRepository) {

//    suspend fun login(email:String, password: String) {
//        if (email.isEmpty() || password.isEmpty()) {
//            throw IllegalArgumentException("username or password cannot be empty")
//        } else {
//             val response = authRepository.login(email, password)
//            authRepository.saveSession(response)
//
//        }
//    }





}