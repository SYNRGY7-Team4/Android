package com.synrgy7team4.feature_transfer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synrgy7team4.common.Log
import com.synrgy7team4.common.TokenHandler
import com.synrgy7team4.common.UserHandler
import com.synrgy7team4.domain.feature_transfer.model.request.AccountSaveRequest
import com.synrgy7team4.domain.feature_transfer.model.request.BalanceSetRequest
import com.synrgy7team4.domain.feature_transfer.model.request.TransferRequest
import com.synrgy7team4.domain.feature_transfer.model.response.AccountSaveResponseDomain
import com.synrgy7team4.domain.feature_transfer.model.response.AccountsResponseItemDomain
import com.synrgy7team4.domain.feature_transfer.model.response.BalanceGetResponseDomain
import com.synrgy7team4.domain.feature_transfer.model.response.BalanceSetResponseDomain
import com.synrgy7team4.domain.feature_transfer.model.response.MutationGetDataDomain
import com.synrgy7team4.domain.feature_transfer.model.response.SavedAccountsGetResponseDomain
import com.synrgy7team4.domain.feature_transfer.model.response.TransferResponseDomain
import com.synrgy7team4.domain.feature_transfer.usecase.HttpExceptionUseCase
import com.synrgy7team4.domain.feature_transfer.usecase.TransferUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransferViewModel(
    private val transferUseCase: TransferUseCase,
    private val tokenHandler: TokenHandler,
    private val userHandler: UserHandler
) : ViewModel() {
    lateinit var jwtToken: String
    lateinit var accountNumber: String
    lateinit var accountPin: String
    lateinit var userName: String

    private val _mutationData = MutableLiveData<MutationGetDataDomain>()
    val mutationData: LiveData<MutationGetDataDomain> = _mutationData

    private val _balanceData = MutableLiveData<BalanceGetResponseDomain>()
    val balanceData: LiveData<BalanceGetResponseDomain> = _balanceData

    private val _transferResult = MutableLiveData<TransferResponseDomain>()
    val transferResult: LiveData<TransferResponseDomain> = _transferResult

    private val _savedAccountsData = MutableLiveData<SavedAccountsGetResponseDomain>()
    val savedAccountsData: LiveData<SavedAccountsGetResponseDomain> = _savedAccountsData

    private val _accountSaveResponse = MutableLiveData<AccountSaveResponseDomain>()
    val accountSaveResponse: LiveData<AccountSaveResponseDomain> = _accountSaveResponse

    private val _balanceSetResult = MutableLiveData<BalanceSetResponseDomain>()
    val balanceSetResponse: LiveData<BalanceSetResponseDomain> = _balanceSetResult

    private val _accountAllList = MutableLiveData<AccountsResponseItemDomain?>()
    val accountAllList: LiveData<AccountsResponseItemDomain?> = _accountAllList

    private val _screenshotPath = MutableLiveData<String>()
    val screenshotPath: LiveData<String> = _screenshotPath

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun initializeData(): Deferred<Unit> = viewModelScope.async {
        if (tokenHandler.isTokenExpired()) {
            tokenHandler.handlingTokenExpire()
        } else {
            jwtToken = tokenHandler.loadJwtToken() ?: throw Exception("JWT token tidak tersedia")
            accountNumber = userHandler.loadAccountNumber() ?: throw Exception("Nomor akun tidak tersedia")
            accountPin = userHandler.loadAccountPin() ?: throw Exception("PIN akun tidak tersedia")
            userName = userHandler.loadUserName() ?: throw Exception("Nama pengguna tidak tersedia")
        }
    }

    fun transfer(
        pin: String,
        accountTo: String,
        amount: Int,
        description: String,
        datetime: String,
        destinationBank: String
    ) = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                Log.d("Andre", "pin: $pin")
                val transferRequest = TransferRequest(
                    accountFrom = accountNumber,
                    accountTo = accountTo,
                    amount = amount,
                    description = description,
                    pin = pin,
                    datetime = datetime,
                    destinationBank = destinationBank
                )
                val transferResponse = transferUseCase.transfer("Bearer $jwtToken", transferRequest)
                _transferResult.value = transferResponse
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun saveAccount(accountNumber: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                val accountSaveRequest = AccountSaveRequest(accountNumber)
                val accountSaveResponse = transferUseCase.saveAccount("Bearer $jwtToken", accountSaveRequest)
                _accountSaveResponse.value = accountSaveResponse
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun getSavedAccounts() = viewModelScope.launch {
        _isLoading.value = true
         try {
             if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                 // Assuming transferUseCase.getSavedAccounts can return null
            val response = transferUseCase.getSavedAccounts("Bearer $jwtToken") ?: SavedAccountsGetResponseDomain()
            _savedAccountsData.value = response
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
            _savedAccountsData.value = SavedAccountsGetResponseDomain(
                data = emptyList(),
                success = false,
                message = "An error occurred: ${e.message}"
            )
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun setBalance(newBalance: Double) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val balanceSetRequest = BalanceSetRequest(accountNumber, newBalance)
            val balanceSetResponse =
                transferUseCase.setBalance("Bearer $jwtToken", balanceSetRequest)
            _balanceSetResult.value = balanceSetResponse
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun getBalance() = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                val balanceGetResponse = transferUseCase.getBalance("Bearer $jwtToken", accountNumber)
                _balanceData.value = balanceGetResponse
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun getMutation(id: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                val response = transferUseCase.getMutation("Bearer $jwtToken", id)
                _mutationData.value = response.data!!
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }

    fun cekAccountList(accountNumber: String) = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (tokenHandler.isTokenExpired()) {
                tokenHandler.handlingTokenExpire()
            } else {
                val response = transferUseCase.checkAccount("Bearer $jwtToken")
                val filteredAccount = response.find { it.accountNumber == accountNumber }
                _accountAllList.value = filteredAccount
                Log.d("Andre", _accountAllList.value.toString())
            }
        } catch (e: HttpExceptionUseCase) {
            _error.value = e
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _isLoading.value = false
        }
    }



    // Fungsi untuk menyimpan path tangkapan layar
    fun saveScreenshotPath(path: String) {

        _screenshotPath.value = path
        Log.d("TransferVM", _screenshotPath.value.toString())

    }
}
