package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String = "",
    val name: String = "",
    @SerialName("captain_id")
    val captainId: String = "",
    val members: List<String> = emptyList(),
    @SerialName("tournament_id")
    val tournamentId: String = "",
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis()
)
