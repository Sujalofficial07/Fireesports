package com.fireesports.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tournament(
    val id: String = "",
    val title: String = "",
    val game: String = "",
    val description: String = "",
    val rules: String = "",
    @SerialName("image_url")
    val imageUrl: String = "",
    @SerialName("entry_fee")
    val entryFee: Double = 0.0,
    @SerialName("prize_pool")
    val prizePool: Double = 0.0,
    @SerialName("max_participants")
    val maxParticipants: Int = 100,
    @SerialName("current_participants")
    val currentParticipants: Int = 0,
    @SerialName("team_size")
    val teamSize: Int = 1,
    @SerialName("start_time")
    val startTime: Long = 0L,
    @SerialName("end_time")
    val endTime: Long = 0L,
    @SerialName("registration_deadline")
    val registrationDeadline: Long = 0L,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    @SerialName("created_by")
    val createdBy: String = "",
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
enum class TournamentStatus {
    UPCOMING, REGISTRATION_OPEN, LIVE, COMPLETED, CANCELLED
}

@Serializable
data class TournamentParticipant(
    val id: String = "",
    @SerialName("tournament_id")
    val tournamentId: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("team_id")
    val teamId: String? = null,
    @SerialName("joined_at")
    val joinedAt: Long = System.currentTimeMillis(),
    val status: ParticipantStatus = ParticipantStatus.ACTIVE
)

@Serializable
enum class ParticipantStatus {
    ACTIVE, ELIMINATED, WINNER, DISQUALIFIED
}
