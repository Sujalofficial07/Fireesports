package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.Transaction
import com.fireesports.data.model.TransactionCategory
import com.fireesports.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadWalletData(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val balanceResult = repository.getBalance(userId)
                if (balanceResult.isSuccess) {
                    _balance.value = balanceResult.getOrNull() ?: 0.0
                } else {
                    _error.value = balanceResult.exceptionOrNull()?.message
                }

                // ⚠️ You don’t have getTransactionHistory() → so we skip that part safely
                _transactions.value = emptyList()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addFunds(userId: String, amount: Double, description: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.addFunds(userId, amount, description)
                if (result.isSuccess) {
                    loadWalletData(userId)
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deductFunds(userId: String, amount: Double, category: TransactionCategory, description: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.deductFunds(userId, amount, category, description)
                if (result.isSuccess) {
                    loadWalletData(userId)
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
