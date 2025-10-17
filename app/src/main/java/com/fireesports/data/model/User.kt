package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    @SerialName("gamer_id")
    val gamerId: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("wallet_balance")
    val walletBalance: Double = 0.0,
    val xp: Int = 0,
    val level: Int = 1,
    val role: UserRole = UserRole.PLAYER,
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @SerialName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @SerialName("is_online")
    val isOnline: Boolean = false,
    val language: String = "en"
)

@Serializable
enum class UserRole {
    PLAYER, ADMIN, MODERATOR
}
