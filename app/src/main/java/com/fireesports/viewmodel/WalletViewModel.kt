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

    // -------------------------------
    // UI States
    // -------------------------------

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _uiState = MutableStateFlow<WalletUiState>(WalletUiState.Loading)
    val uiState: StateFlow<WalletUiState> = _uiState

    // -------------------------------
    // Initialization
    // -------------------------------

    init {
        loadWalletData()
    }

    // -------------------------------
    // Core Wallet Logic
    // -------------------------------

    private fun loadWalletData() {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: run {
                _uiState.value = WalletUiState.Error("User not logged in")
                return@launch
            }

            _uiState.value = WalletUiState.Loading

            // Load balance
            walletRepository.getBalance(userId)
                .onSuccess { balance ->
                    _balance.value = balance
                }
                .onFailure { e ->
                    _uiState.value = WalletUiState.Error(e.message ?: "Failed to load balance")
                }

            // Load transactions
            walletRepository.getTransactionHistory(userId)
                .onSuccess { history ->
                    _transactions.value = history
                    _uiState.value = WalletUiState.Success
                }
                .onFailure { e ->
                    _uiState.value = WalletUiState.Error(e.message ?: "Failed to load transactions")
                }
        }
    }

    fun addFunds(amount: Double, description: String = "Funds added") {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: run {
                _uiState.value = WalletUiState.Error("User not logged in")
                return@launch
            }

            _uiState.value = WalletUiState.Loading

            walletRepository.addFunds(userId, amount, description)
                .onSuccess {
                    loadWalletData()
                }
                .onFailure { e ->
                    _uiState.value = WalletUiState.Error(e.message ?: "Failed to add funds")
                }
        }
    }

    fun refresh() {
        loadWalletData()
    }
}

// -------------------------------
// UI State Class
// -------------------------------

sealed class WalletUiState {
    object Loading : WalletUiState()
    object Success : WalletUiState()
    data class Error(val message: String) : WalletUiState()
}
