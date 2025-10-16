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
                .select {
                    filter { eq("id", id) }
                }
                .decodeList<Tournament>()
            
            Result.success(tournaments.firstOrNull() ?: throw Exception("Tournament not found"))
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
                .select {
                    filter { eq("userId", userId) }
                }
                .decodeList<TournamentParticipant>()
            
            val tournamentIds = participants.map { it.tournamentId }
            if (tournamentIds.isEmpty()) {
                return Result.success(emptyList())
            }
            
            val tournaments = supabase.from("tournaments")
                .select {
                    filter { isIn("id", tournamentIds) }
                }
                .decodeList<Tournament>()
            
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTournaments(query: String): Result<List<Tournament>> {
        return try {
            val allTournaments = supabase.from("tournaments")
                .select()
                .decodeList<Tournament>()
            
            // Filter locally since Supabase 2.6.0 might not support complex filters
            val filtered = allTournaments.filter { tournament ->
                tournament.title.contains(query, ignoreCase = true) ||
                tournament.game.contains(query, ignoreCase = true)
            }
            
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
