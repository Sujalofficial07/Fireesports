package com.fireesports.data.model

import java.util.Date

// User Model
data class User(
    val id: String,
    val username: String,
    val email: String,
    val gamerId: String? = null,
    val avatarUrl: String? = null,
    val phoneNumber: String? = null,
    val language: String = "en",
    val role: UserRole = UserRole.PLAYER,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class UserRole {
    PLAYER, ADMIN
}

// Tournament Models
data class Tournament(
    val id: String,
    val name: String,
    val game: String,
    val description: String,
    val rules: String,
    val entryFee: Int,
    val prizePool: Int,
    val startTime: Date,
    val maxTeams: Int,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    val registeredTeams: List<String> = emptyList() // List of Team IDs
)

enum class TournamentStatus {
    UPCOMING, ONGOING, COMPLETED, CANCELED
}

// Chat Models
data class ChatRoom(
    val id: String,
    val name: String,
    val type: ChatType,
    val members: List<String> // List of User IDs
)

data class ChatMessage(
    val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String, // Denormalized for easy display
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ChatType {
    GLOBAL, TEAM
}

// Wallet Model
data class WalletTransaction(
    val id: String,
    val userId: String,
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
