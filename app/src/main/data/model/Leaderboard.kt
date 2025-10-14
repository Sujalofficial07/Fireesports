package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val userId: String = "",
    val username: String = "",
    val avatarUrl: String? = null,
    val xp: Int = 0,
    val level: Int = 1,
    val wins: Int = 0,
    val tournamentsPlayed: Int = 0,
    val rank: Int = 0
)
