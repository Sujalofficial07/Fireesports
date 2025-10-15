package com.fireesports.data.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val email: String,
    val gamerId: String,
    val level: Int = 1,
    val xp: Int = 0,
    val walletBalance: Double = 0.0
)

data class Tournament(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val game: String,
    val prizePool: Double,
    val entryFee: Double,
    val maxParticipants: Int,
    val currentParticipants: Int = 0,
    val startTime: Long,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    val description: String = "",
    val rules: String = ""
)

enum class TournamentStatus {
    UPCOMING,
    ONGOING,
    COMPLETED,
    CANCELLED
}

data class ChatRoom(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: ChatType,
    val participantIds: List<String>,
    val lastMessage: String? = null,
    val lastMessageTime: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val chatRoomId: String,
    val senderId: String,
    val senderName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ChatType {
    GLOBAL,
    TEAM,
    PRIVATE
}
