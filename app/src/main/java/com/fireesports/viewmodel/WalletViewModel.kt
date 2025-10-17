package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.Transaction
import com.fireesports.data.repository.AuthRepository
import com.fireesports.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _uiState = MutableStateFlow<WalletUiState>(WalletUiState.Loading)
    val uiState: StateFlow<WalletUiState> = _uiState

    init {
        loadWalletData()
    }

    private fun loadWalletData() {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: return@launch
            _uiState.value = WalletUiState.Loading

            // ✅ Load balance
            val balanceResult = walletRepository.getBalance(userId)
            if (balanceResult.isSuccess) {
                _balance.value = balanceResult.getOrDefault(0.0)
            } else {
                _uiState.value = WalletUiState.Error(balanceResult.exceptionOrNull()?.message ?: "Failed to load balance")
            }

            // ✅ Load transactions
            val transactionResult = walletRepository.getTransactionHistory(userId)
            if (transactionResult.isSuccess) {
                _transactions.value = transactionResult.getOrDefault(emptyList())
                _uiState.value = WalletUiState.Success
            } else {
                _uiState.value = WalletUiState.Error(transactionResult.exceptionOrNull()?.message ?: "Failed to load transactions")
            }
        }
    }

    fun addFunds(amount: Double, description: String = "Funds added") {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: return@launch
            _uiState.value = WalletUiState.Loading

            val result = walletRepository.addFunds(userId, amount, description)
            if (result.isSuccess) {
                loadWalletData()
            } else {
                _uiState.value = WalletUiState.Error(result.exceptionOrNull()?.message ?: "Failed to add funds")
            }
        }
    }

    fun refresh() {
        loadWalletData()
    }
}

sealed class WalletUiState {
    object Loading : WalletUiState()
    object Success : WalletUiState()
    data class Error(val message: String) : WalletUiState()
}
