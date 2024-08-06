package com.synrgy7team4.feature_auth.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.synrgy7team4.feature_auth.data.remote.request.LoginRequest
import com.synrgy7team4.feature_auth.data.remote.response.ErrorResponse
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.data.remote.response.RegisterResponse
import com.synrgy7team4.feature_auth.data.repository.ImplementAuthRepository
import com.synrgy7team4.feature_auth.domain.model.UserModel
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import com.synrgy7team4.feature_auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: AuthRepository): ViewModel() {



    private val _token = MutableLiveData<String?>()
    val token:LiveData<String?> = _token


    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful:LiveData<Boolean> = _isSuccessful

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val loginRequest = LoginRequest(email, password)
                val loginResponse = repository.login(loginRequest)
                _isSuccessful.value = loginResponse.success
                _token.value = loginResponse.data?.jwtToken
                Log.d("LoginViewModel", "LoginRequest: $loginRequest")



            } catch (throwable: Throwable) {
                if (throwable is HttpException) {
                    val json = throwable.response()?.errorBody()?.string()
                    val error = Gson().fromJson(json, ErrorResponse::class.java)
                    _error.value = false
                    Log.e("LoginViewModel", "HTTP Error: ${error.message}")
                } else {
                    _error.value = false
                    Log.e("LoginViewModel", "Error: ${throwable.message}")

                }
            } finally {
                _isLoading.value = false

            }
        }

    }







}