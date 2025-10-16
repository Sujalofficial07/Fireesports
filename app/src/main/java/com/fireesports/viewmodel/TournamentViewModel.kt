package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TransactionCategory
import com.fireesports.data.repository.AuthRepository
import com.fireesports.data.repository.TournamentRepository
import com.fireesports.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val tournamentRepository: TournamentRepository,
    private val walletRepository: WalletRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _tournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val tournaments: StateFlow<List<Tournament>> = _tournaments

    private val _selectedTournament = MutableStateFlow<Tournament?>(null)
    val selectedTournament: StateFlow<Tournament?> = _selectedTournament

    private val _uiState = MutableStateFlow<TournamentUiState>(TournamentUiState.Loading)
    val uiState: StateFlow<TournamentUiState> = _uiState

    init {
        loadTournaments()
    }

    fun loadTournaments() {
        viewModelScope.launch {
            _uiState.value = TournamentUiState.Loading
            tournamentRepository.getTournaments().fold(
                onSuccess = { 
                    _tournaments.value = it
                    _uiState.value = TournamentUiState.Success
                },
                onFailure = {
                    _uiState.value = TournamentUiState.Error(it.message ?: "Failed to load tournaments")
                }
            )
        }
    }

    fun loadTournamentDetails(tournamentId: String) {
        viewModelScope.launch {
            tournamentRepository.getTournamentById(tournamentId).fold(
                onSuccess = { _selectedTournament.value = it },
                onFailure = { _uiState.value = TournamentUiState.Error(it.message ?: "Failed to load tournament") }
            )
        }
    }

    fun joinTournament(tournamentId: String) {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: return@launch
            val tournament = _selectedTournament.value ?: return@launch
            
            _uiState.value = TournamentUiState.Joining
            
            if (tournament.entryFee > 0) {
                walletRepository.deductFunds(
                    userId = userId,
                    amount = tournament.entryFee,
                    category = TransactionCategory.TOURNAMENT_ENTRY,
                    description = "Entry fee for ${tournament.title}",
                    referenceId = tournamentId
                ).fold(
                    onSuccess = { proceedWithJoin(tournamentId, userId) },
                    onFailure = { _uiState.value = TournamentUiState.Error("Insufficient balance") }
                )
            } else {
                proceedWithJoin(tournamentId, userId)
            }
        }
    }

    private suspend fun proceedWithJoin(tournamentId: String, userId: String) {
        tournamentRepository.joinTournament(tournamentId, userId).fold(
            onSuccess = { 
                _uiState.value = TournamentUiState.JoinSuccess
                loadTournamentDetails(tournamentId)
            },
            onFailure = {
                _uiState.value = TournamentUiState.Error(it.message ?: "Failed to join tournament")
            }
        )
    }

    fun searchTournaments(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadTournaments()
                return@launch
            }
            
            tournamentRepository.searchTournaments(query).fold(
                onSuccess = { _tournaments.value = it },
                onFailure = { _uiState.value = TournamentUiState.Error(it.message ?: "Search failed") }
            )
        }
    }

    fun resetState() {
        _uiState.value = TournamentUiState.Success
    }
}

sealed class TournamentUiState {
    object Loading : TournamentUiState()
    object Success : TournamentUiState()
    object Joining : TournamentUiState()
    object JoinSuccess : TournamentUiState()
    data class Error(val message: String) : TournamentUiState()
}
