package com.fireesports.data.model

import kotlinx.serialization.Serializable

enum class TournamentStatus { UPCOMING, ONGOING, COMPLETED, CANCELED }

@Serializable
data class Tournament(
    val id: String,
    val title: String, // Fixes "Unresolved reference: title"
    val game: String,
    val prizePool: Int,
    val startTime: Long, // Fixes Date() constructor error
    val currentParticipants: Int, // Fixes "Unresolved reference"
    val maxParticipants: Int, // Fixes "Unresolved reference"
    val status: TournamentStatus
)
