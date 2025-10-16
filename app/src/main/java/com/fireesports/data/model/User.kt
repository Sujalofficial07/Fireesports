package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val gamerId: String = "",
    val avatarUrl: String? = null,
    val phoneNumber: String? = null,
    val walletBalance: Double = 0.0,
    val xp: Int = 0,
    val level: Int = 1,
    val role: UserRole = UserRole.PLAYER,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val language: String = "en"
)

@Serializable
enum class UserRole {
    PLAYER, ADMIN, MODERATOR
}
