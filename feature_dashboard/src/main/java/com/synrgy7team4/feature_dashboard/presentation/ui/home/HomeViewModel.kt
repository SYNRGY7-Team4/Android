package com.synrgy7team4.feature_dashboard.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synrgy7team4.feature_dashboard.data.remote.retrofit.provideService
import com.synrgy7team4.feature_dashboard.data.remote.response.BalanceResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    private val _data = MutableLiveData<BalanceResponse>()
    val data: LiveData<BalanceResponse> = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun fectData(token: String, accountNumber: String ) {

        viewModelScope.launch {

            try {
                val response = provideService().getBalance("Bearer $token", accountNumber)
                _data.postValue(response)

            } catch (e: HttpException){
                val errorBody = e.response()?.body()?.toString()
                _error.postValue("HTTP Error: ${e.message()} - $errorBody")
            } catch (e: Exception) {
                _error.postValue("An unexpected error occurred: ${e.message}")
            }



        }

    }



}