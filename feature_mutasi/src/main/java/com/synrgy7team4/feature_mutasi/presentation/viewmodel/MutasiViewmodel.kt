package com.synrgy7team4.feature_mutasi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.synrgy7team4.feature_mutasi.data.response.MutationData
import com.synrgy7team4.feature_mutasi.data.Repository
import com.synrgy7team4.feature_mutasi.data.response.MutationGroupedByDate
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MutasiViewmodel(private val repository: Repository) : ViewModel() {

    private val _mutations = MutableLiveData<List<MutationData>>()
    val mutations: LiveData<List<MutationData>> get() = _mutations

    private val _mutationsbydate = MutableLiveData<List<MutationGroupedByDate>>()
    val mutationsbydate: LiveData<List<MutationGroupedByDate>> get() = _mutationsbydate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun filterByDateRange(data: List<MutationData>, startDate: String, endDate: String): List<MutationData> {
        // Define the formatter based on the datetime format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        // Parse the start and end dates
        val start = LocalDateTime.parse(startDate, formatter)
        val end = LocalDateTime.parse(endDate, formatter)

        // Filter the data based on the datetime range
        return data.filter {
            val itemDate = LocalDateTime.parse(it.datetime, formatter)
            itemDate.isAfter(start) && itemDate.isBefore(end)
        }
    }

    // Usage example
    fun groupMutationsByDate(mutations: List<MutationData>): List<MutationGroupedByDate> {
        val grouped = mutations.groupBy {
            LocalDateTime.parse(it.datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).toLocalDate().toString()
        }
        return grouped.map { (date, mutations) ->
            MutationGroupedByDate(date, mutations)
        }
    }

    suspend fun fetchFilteredUserData(startDate: String, endDate: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val accountNumber = repository.getUserData().body()?.data?.accountNumber.toString()
                val response = repository.getMutations(accountNumber)
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        val filteredData = filterByDateRange(userData, startDate, endDate)
                        val groupedData = groupMutationsByDate(filteredData)
                        _mutationsbydate.value = groupedData
                        _isLoading.value = false
                        Log.d("test",groupedData.toString())
                    }
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }


    fun loadMutations() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val accountNumber = repository.getUserData().body()?.data?.accountNumber.toString()
                val response = repository.getMutations(accountNumber)
                if (response.isSuccessful) {
                    _mutations.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Dummy data function for testing
    fun loadDummyData(startDate: String, endDate: String) {
        // List of MutationData with additional entries
        val mutationDataList = listOf(
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174000",
                amount = 5000000,
                datetime = "2024-08-01T12:00:00",
                type = "transfer",
                status = "completed",
                description = "Payment for groceries",
                account_from = "123456789",
                account_to = "987654321"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174001",
                amount = 2000000,
                datetime = "2024-08-02T15:30:00",
                type = "top_up",
                status = "completed",
                description = "Top-up for mobile",
                account_from = "123456789",
                account_to = "112233445"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174002",
                amount = 7500000,
                datetime = "2024-08-03T09:15:00",
                type = "transfer",
                status = "pending",
                description = "Payment for services",
                account_from = "123456789",
                account_to = "998877665"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174003",
                amount = 15000000,
                datetime = "2024-08-01T14:00:00",
                type = "top_up",
                status = "completed",
                description = "ATM withdrawal",
                account_from = "123456789",
                account_to = "N/A"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174004",
                amount = 3000000,
                datetime = "2024-08-04T10:00:00",
                type = "transfer",
                status = "completed",
                description = "Payment for utilities",
                account_from = "987654321",
                account_to = "123456789"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174005",
                amount = 12000000,
                datetime = "2024-08-03T16:30:00",
                type = "top_up",
                status = "completed",
                description = "Top-up for subscription",
                account_from = "123456789",
                account_to = "556677889"
            ),
            MutationData(
                id = "123e4567-e89b-12d3-a456-426614174006",
                amount = 4500000,
                datetime = "2024-08-05T11:00:00",
                type = "transfer",
                status = "completed",
                description = "Payment for rent",
                account_from = "112233445",
                account_to = "123456789"
            )
        )

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val filteredData = filterByDateRange(mutationDataList, startDate, endDate)
                val groupedData = groupMutationsByDate(filteredData)
                _mutationsbydate.value = groupedData
                Log.d("test",groupedData.toString())
                _isLoading.value = false
            } catch (e: Exception) {
                // Handle exception
            }
        }

        // Group by date part of the datetime

    }

}
