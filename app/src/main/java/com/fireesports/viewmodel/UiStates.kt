package com.fireesports.viewmodel

import com.fireesports.data.model.Tournament
import com.fireesports.data.model.User

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    object ProfileCreated : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

sealed class TournamentUiState {
    object Loading : TournamentUiState()
    data class Success(val tournament: Tournament) : TournamentUiState()
    data class Error(val message: String) : TournamentUiState()
}

sealed class WalletUiState {
    object Loading : WalletUiState()
    data class Success(val balance: Double) : WalletUiState()
    data class Error(val message: String) : WalletUiState()
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Success : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
