package com.fireesports.data.model

import kotlinx.serialization.Serializable

enum class TournamentStatus {
    UPCOMING, REGISTRATION_OPEN, LIVE, COMPLETED, CANCELLED
}

@Serializable
data class TournamentParticipant(
    val id: String = "",
    val tournamentId: String = "",
    val userId: String = "",
    val teamId: String? = null,
    val joinedAt: Long = System.currentTimeMillis(),
    val status: ParticipantStatus = ParticipantStatus.ACTIVE
)

enum class ParticipantStatus {
    ACTIVE, ELIMINATED, WINNER, DISQUALIFIED
}
