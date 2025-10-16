package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.Tournament
import com.fireesports.data.repository.TournamentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _featuredTournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val featuredTournaments: StateFlow<List<Tournament>> = _featuredTournaments

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadFeaturedTournaments()
    }

    private fun loadFeaturedTournaments() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            tournamentRepository.getTournaments().fold(
                onSuccess = { tournaments ->
                    _featuredTournaments.value = tournaments.take(5)
                    _uiState.value = HomeUiState.Success
                },
                onFailure = {
                    _uiState.value = HomeUiState.Error(it.message ?: "Failed to load tournaments")
                }
            )
        }
    }

    fun refresh() {
        loadFeaturedTournaments()
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object Success : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
