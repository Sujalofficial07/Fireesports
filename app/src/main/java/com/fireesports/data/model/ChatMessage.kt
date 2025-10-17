package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String = "",
    @SerialName("chat_room_id")
    val chatRoomId: String = "",
    @SerialName("sender_id")
    val senderId: String = "",
    @SerialName("sender_name")
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    @SerialName("is_read")
    val isRead: Boolean = false
)

@Serializable
data class ChatRoom(
    val id: String = "",
    val type: ChatType = ChatType.GLOBAL,
    val name: String = "",
    val participants: List<String> = emptyList(),
    @SerialName("last_message")
    val lastMessage: String? = null,
    @SerialName("last_message_time")
    val lastMessageTime: Long = 0L
)

@Serializable
enum class ChatType {
    GLOBAL, TEAM, PRIVATE
}
