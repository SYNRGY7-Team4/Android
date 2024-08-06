package com.synrgy7team4.feature_auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.synrgy7team4.feature_auth.data.remote.response.ErrorResponse
import com.synrgy7team4.feature_auth.data.remote.response.RegistResponse
import com.synrgy7team4.feature_auth.data.remote.request.RegisterRequest
import com.synrgy7team4.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {


    private val _registerResult = MutableLiveData<RegistResponse?>()
    val registerResult: LiveData<RegistResponse?> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<ErrorResponse>()
    val error: LiveData<ErrorResponse> = _error


    fun registerUser(
        email: String,
        hp: String,
        password: String,
        nik: String,
        name: String,
        date: String,
        pin: String,
        ektp_photo: String,
    ) {



        viewModelScope.launch {
            _isLoading.value = true

            try {
                val registerRequest = RegisterRequest(email, hp, password, nik, name, date, pin, ektp_photo)
                val response = authRepository.register(registerRequest)

                _registerResult.postValue(response)
                Log.d("RegisterViewModel", "RegisterRequest: $registerRequest")

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _error.postValue(errorBody)
                Log.e("RegisterViewModel", "HTTP Error: $errorMessage")
            } finally {
                _isLoading.value = false

            }

        }

    }






}