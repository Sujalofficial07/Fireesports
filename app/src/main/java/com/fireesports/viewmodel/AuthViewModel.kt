package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.User
import com.fireesports.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    val currentUser: StateFlow<User?> = authRepository.currentUser

    init {
        // Auto-check session on app start
        if (currentUser.value != null) {
            _uiState.value = AuthUiState.SignInSuccess
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.signUp(email, password).fold(
                onSuccess = { _uiState.value = AuthUiState.SignUpSuccess },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Sign up failed") }
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.signIn(email, password).fold(
                onSuccess = { _uiState.value = AuthUiState.SignInSuccess },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Sign in failed") }
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.signInWithGoogle(idToken).fold(
                onSuccess = { _uiState.value = AuthUiState.SignInSuccess },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Google sign-in failed") }
            )
        }
    }

    fun createProfile(username: String, gamerId: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.createUserProfile(username, gamerId).fold(
                onSuccess = { _uiState.value = AuthUiState.ProfileCreated },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Profile creation failed") }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = AuthUiState.SignedOut
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object SignUpSuccess : AuthUiState()
    object SignInSuccess : AuthUiState()
    object ProfileCreated : AuthUiState()
    object SignedOut : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
