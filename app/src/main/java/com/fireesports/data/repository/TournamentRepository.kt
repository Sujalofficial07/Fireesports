package com.fireesports.data.repository

import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TournamentParticipant
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TournamentRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider
) {
    private val supabase = supabaseProvider.client

    suspend fun getTournaments(): Result<List<Tournament>> {
        return try {
            val tournaments = supabase.from("tournaments")
                .select()
                .decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTournamentById(id: String): Result<Tournament> {
        return try {
            val tournaments = supabase.from("tournaments")
                .select() {
                    filter { eq("id", id) }
                }.decodeList<Tournament>()
            Result.success(tournaments.first())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinTournament(tournamentId: String, userId: String, teamId: String? = null): Result<Unit> {
        return try {
            val participant = TournamentParticipant(
                tournamentId = tournamentId,
                userId = userId,
                teamId = teamId
            )
            supabase.from("tournament_participants").insert(participant)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyTournaments(userId: String): Result<List<Tournament>> {
        return try {
            val participants = supabase.from("tournament_participants")
                .select() {
                    filter { eq("userId", userId) }
                }.decodeList<TournamentParticipant>()
            
            val tournamentIds = participants.map { it.tournamentId }
            val tournaments = supabase.from("tournaments")
                .select() {
                    filter { isIn("id", tournamentIds) }
                }.decodeList<Tournament>()
            
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTournaments(query: String): Result<List<Tournament>> {
        return try {
            val tournaments = supabase.from("tournaments")
                .select() {
                    or {
                        ilike("title", "%$query%")
                        ilike("game", "%$query%")
                    }
                }.decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
