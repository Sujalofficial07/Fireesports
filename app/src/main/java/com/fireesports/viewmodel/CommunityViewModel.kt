package com.fireesports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fireesports.data.model.ChatMessage
import com.fireesports.data.model.ChatRoom
import com.fireesports.data.repository.AuthRepository
import com.fireesports.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CommunityUiState {
    object Loading : CommunityUiState()
    object Success : CommunityUiState()
    data class Error(val message: String) : CommunityUiState()
}

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState: StateFlow<CommunityUiState> = _uiState

    init {
        loadChatRooms()
    }

    private fun loadChatRooms() {
        viewModelScope.launch {
            val userId = authRepository.currentUser.value?.id ?: return@launch
            
            chatRepository.getChatRooms(userId).fold(
                onSuccess = { 
                    _chatRooms.value = it
                    _uiState.value = CommunityUiState.Success
                },
                onFailure = {
                    _uiState.value = CommunityUiState.Error(it.message ?: "Failed to load chat rooms")
                }
            )
        }
    }

    fun loadMessages(chatRoomId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(chatRoomId).fold(
                onSuccess = { _messages.value = it },
                onFailure = { _uiState.value = CommunityUiState.Error(it.message ?: "Failed to load messages") }
            )
            
            // Subscribe to real-time updates
            chatRepository.subscribeToMessages(chatRoomId).collect { newMessage ->
                _messages.value = _messages.value + newMessage
            }
        }
    }

    fun sendMessage(chatRoomId: String, message: String) {
        viewModelScope.launch {
            val user = authRepository.currentUser.value ?: return@launch
            
            val chatMessage = ChatMessage(
                chatRoomId = chatRoomId,
                senderId = user.id,
                senderName = user.username,
                message = message
            )
            
            chatRepository.sendMessage(chatMessage)
        }
    }
}
