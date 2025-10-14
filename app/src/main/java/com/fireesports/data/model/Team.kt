package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String = "",
    val name: String = "",
    val captainId: String = "",
    val members: List<String> = emptyList(),
    val tournamentId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
