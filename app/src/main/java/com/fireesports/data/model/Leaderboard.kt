package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    @SerialName("user_id")
    val userId: String = "",
    val username: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val wins: Int = 0,
    @SerialName("tournaments_played")
    val tournamentsPlayed: Int = 0,
    val rank: Int = 0
)
