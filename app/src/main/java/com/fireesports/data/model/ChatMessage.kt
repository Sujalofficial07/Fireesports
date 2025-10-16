package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String = "",
    val chatRoomId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Serializable
data class ChatRoom(
    val id: String = "",
    val type: ChatType = ChatType.GLOBAL,
    val name: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageTime: Long = 0L
)

@Serializable
enum class ChatType {
    GLOBAL, TEAM, PRIVATE
}
