package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long
)

enum class ChatType { GLOBAL, TEAM }

@Serializable
data class ChatRoom(
    val id: String,
    val name: String,
    val type: ChatType,
    val members: List<String>
)
