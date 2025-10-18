package com.fireesports.data.repository

import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TournamentStatus
import com.fireesports.data.model.User
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider
) {
    private val supabase = supabaseProvider.client

    suspend fun createTournament(tournament: Tournament): Result<Unit> {
        return try {
            supabase.from("tournaments").insert(tournament)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTournaments(): Result<List<Tournament>> {
        return try {
            val tournaments = supabase.from("tournaments")
                .select()
                .decodeList<Tournament>()
            Result.success(tournaments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTournamentStatus(tournamentId: String, status: TournamentStatus): Result<Unit> {
        return try {
            supabase.from("tournaments").update(mapOf("status" to status.name)) {
                filter { eq("id", tournamentId) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTournament(tournamentId: String): Result<Unit> {
        return try {
            supabase.from("tournaments").delete {
                filter { eq("id", tournamentId) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val users = supabase.from("users")
                .select()
                .decodeList<User>()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
