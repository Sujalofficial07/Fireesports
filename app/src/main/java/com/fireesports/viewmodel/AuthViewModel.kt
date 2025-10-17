package com.fireesports.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.User
import com.fireesports.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
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

    // Get Google Sign-In intent
    fun getGoogleSignInIntent(): Intent {
        return authRepository.createGoogleSignInIntent()
    }

    // Handle Google Sign-In result
    fun handleGoogleSignInResult(result: ActivityResult) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                
                authRepository.signInWithGoogle(account).fold(
                    onSuccess = { _uiState.value = AuthUiState.SignInSuccess },
                    onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Google sign in failed") }
                )
            } catch (e: ApiException) {
                _uiState.value = AuthUiState.Error("Google sign in failed: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("Google sign in failed: ${e.message}")
            }
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
