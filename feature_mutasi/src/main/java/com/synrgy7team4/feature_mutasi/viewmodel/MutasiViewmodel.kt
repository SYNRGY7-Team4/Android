package com.synrgy7team4.feature_mutasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synrgy7team4.common.Log
import com.synrgy7team4.common.TokenHandler
import com.synrgy7team4.common.UserHandler
import com.synrgy7team4.domain.feature_mutasi.model.response.MutationDataDomain
import com.synrgy7team4.domain.feature_mutasi.usecase.HttpExceptionUseCase
import com.synrgy7team4.domain.feature_mutasi.usecase.MutasiUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MutasiViewmodel(
    private val mutasiUseCase: MutasiUseCase,
    private val tokenHandler: TokenHandler,
    private val userHandler: UserHandler
) : ViewModel() {
    private val _mutationData = MutableLiveData<List<MutationDataDomain>?>()
    private val _accountNumber = MutableLiveData<String?>()

    val mutationData: LiveData<Pair<List<MutationDataDomain>?, String?>> = MediatorLiveData<Pair<List<MutationDataDomain>?, String?>>().apply {
        addSource(_mutationData) { mutationList ->
            value = Pair(mutationList, _accountNumber.value)
        }
        addSource(_accountNumber) { accountNum ->
            value = Pair(_mutationData.value, accountNum)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getMutationData(
        startDate: String? = null,
        endDate: String? = null,
        type: String = "transfer"
    ) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val jwtToken = tokenHandler.loadJwtToken() ?: throw Exception("JWT token tidak tersedia")
            _accountNumber.value = userHandler.loadAccountNumber() ?: throw Exception("Nomor akun tidak tersedia")
            _mutationData.value =
                if (startDate == null && endDate == null) {
                    mutasiUseCase.getAllMutations("Bearer $jwtToken", _accountNumber.value!!).data
                } else {
                    mutasiUseCase.getMutationsByDate(
                        jwtToken = "Bearer $jwtToken",
                        accountNumber = _accountNumber.value!!,
                        startDate = startDate!!,
                        endDate = endDate!!,
                        type = type
                    ).data?.asReversed()
                }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun filterMutationByType(
        mutationData: List<MutationDataDomain>,
        transactionType: String
    ): List<MutationDataDomain> {
        Log.d("MutasiViewModel", "Filtering mutations by type: $transactionType")
        val filteredData = when (transactionType) {
            "Semua" -> mutationData
            "Uang Masuk" -> mutationData.filter { it.accountFrom != _accountNumber.value!! }
            "Uang Keluar" -> mutationData.filter { it.accountFrom == _accountNumber.value!! }
            else -> throw Exception("Jenis transaksi tidak valid")
        }
        return filteredData
    }

//    fun filterMutationByType(
//        mutationData: List<MutationDataDomain>,
//        transactionType: String
//    ) : List<MutationDataDomain> =
//        when (transactionType) {
//            "Semua" -> mutationData
//            "Uang Masuk" -> mutationData.filter { it.accountFrom != _accountNumber.value!! }
//            "Uang Keluar" -> mutationData.filter { it.accountFrom == _accountNumber.value!! }
//            else -> throw Exception("Jenis transaksi tidak valid")
//        }

    fun groupMutationsByDate(mutations: List<MutationDataDomain>): Map<String, List<MutationDataDomain>> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))

        return mutations.groupBy { mutation ->
            val date = inputFormat.parse(mutation.datetime)
            outputFormat.format(date)
        }
    }
}