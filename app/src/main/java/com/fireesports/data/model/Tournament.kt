package com.fireesports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Tournament(
    val id: String = "",
    val title: String = "",
    val game: String = "",
    val description: String = "",
    val rules: String = "",
    val imageUrl: String = "",
    val entryFee: Double = 0.0,
    val prizePool: Double = 0.0,
    val maxParticipants: Int = 100,
    val currentParticipants: Int = 0,
    val teamSize: Int = 1,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val registrationDeadline: Long = 0L,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
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

@Serializable
enum class ParticipantStatus {
    ACTIVE, ELIMINATED, WINNER, DISQUALIFIED
}
