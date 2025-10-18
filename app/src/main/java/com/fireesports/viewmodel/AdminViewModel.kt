package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TournamentStatus
import com.fireesports.data.model.User
import com.fireesports.data.repository.AdminRepository
import com.fireesports.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState: StateFlow<AdminUiState> = _uiState

    private val _tournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val tournaments: StateFlow<List<Tournament>> = _tournaments

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun createTournament(
        title: String,
        game: String,
        description: String,
        rules: String,
        imageUrl: String,
        entryFee: Double,
        prizePool: Double,
        maxParticipants: Int,
        teamSize: Int,
        startTime: Long
    ) {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            
            val userId = authRepository.currentUser.value?.id ?: ""
            val endTime = startTime + (24 * 60 * 60 * 1000) // +1 day
            val regDeadline = startTime - (24 * 60 * 60 * 1000) // -1 day
            
            adminRepository.createTournament(
                Tournament(
                    title = title,
                    game = game,
                    description = description,
                    rules = rules,
                    imageUrl = imageUrl,
                    entryFee = entryFee,
                    prizePool = prizePool,
                    maxParticipants = maxParticipants,
                    teamSize = teamSize,
                    startTime = startTime,
                    endTime = endTime,
                    registrationDeadline = regDeadline,
                    status = TournamentStatus.REGISTRATION_OPEN,
                    createdBy = userId
                )
            ).fold(
                onSuccess = { _uiState.value = AdminUiState.TournamentCreated },
                onFailure = { _uiState.value = AdminUiState.Error(it.message ?: "Failed to create tournament") }
            )
        }
    }

    fun loadAllTournaments() {
        viewModelScope.launch {
            adminRepository.getAllTournaments().fold(
                onSuccess = { _tournaments.value = it },
                onFailure = { _uiState.value = AdminUiState.Error(it.message ?: "Failed to load") }
            )
        }
    }

    fun updateTournamentStatus(tournamentId: String, status: TournamentStatus) {
        viewModelScope.launch {
            adminRepository.updateTournamentStatus(tournamentId, status).fold(
                onSuccess = { loadAllTournaments() },
                onFailure = { _uiState.value = AdminUiState.Error(it.message ?: "Failed to update") }
            )
        }
    }

    fun deleteTournament(tournamentId: String) {
        viewModelScope.launch {
            adminRepository.deleteTournament(tournamentId).fold(
                onSuccess = { loadAllTournaments() },
                onFailure = { _uiState.value = AdminUiState.Error(it.message ?: "Failed to delete") }
            )
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            adminRepository.getAllUsers().fold(
                onSuccess = { _users.value = it },
                onFailure = { _uiState.value = AdminUiState.Error(it.message ?: "Failed to load users") }
            )
        }
    }
}

sealed class AdminUiState {
    object Idle : AdminUiState()
    object Loading : AdminUiState()
    object TournamentCreated : AdminUiState()
    data class Error(val message: String) : AdminUiState()
}
