package com.fireesports.data.repository

import com.fireesports.data.model.User
import com.fireesports.data.model.UserRole
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider
) {
    private val supabase = supabaseProvider.client
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        // Auto-load current session on startup
        val sessionUser = supabase.auth.currentUserOrNull()
        if (sessionUser != null) {
            // Attempt to fetch full profile from DB
            tryLoadProfile(sessionUser.id)
        }
    }

    private fun tryLoadProfile(userId: String) {
        try {
            val users = supabase.from("users")
                .select {
                    filter { eq("id", userId) }
                }
                .decodeList<User>()
            _currentUser.value = users.firstOrNull()
        } catch (_: Exception) {}
    }

    suspend fun signUp(email: String, password: String): Result<Unit> = try {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signIn(email: String, password: String): Result<User> = try {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No user found")
        val users = supabase.from("users")
            .select {
                filter { eq("id", userId) }
            }
            .decodeList<User>()

        val user = users.firstOrNull() ?: createEmptyProfile(userId)
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> = try {
        // Use Supabase OAuth via Google
        supabase.auth.signInWithIdToken(provider = "google", idToken = idToken)

        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No Google user found")
        val email = supabase.auth.currentUserOrNull()?.email.orEmpty()

        val users = supabase.from("users")
            .select {
                filter { eq("id", userId) }
            }
            .decodeList<User>()

        val user = users.firstOrNull() ?: createEmptyProfile(userId, email)
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createUserProfile(username: String, gamerId: String): Result<User> = try {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No user found")
        val email = supabase.auth.currentUserOrNull()?.email ?: ""

        val user = User(
            id = userId,
            email = email,
            username = username,
            gamerId = gamerId,
            role = UserRole.PLAYER
        )

        supabase.from("users").insert(user)
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateProfile(user: User): Result<User> = try {
        supabase.from("users").update({
            User::username setTo user.username
            User::gamerId setTo user.gamerId
            User::avatarUrl setTo user.avatarUrl
            User::phoneNumber setTo user.phoneNumber
            User::language setTo user.language
            User::updatedAt setTo System.currentTimeMillis()
        }) {
            filter { eq("id", user.id) }
        }
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun signOut(): Result<Unit> = try {
        supabase.auth.signOut()
        _currentUser.value = null
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun createEmptyProfile(userId: String, email: String = ""): User {
        val user = User(
            id = userId,
            email = email,
            username = "",
            gamerId = "",
            role = UserRole.PLAYER
        )
        supabase.from("users").insert(user)
        return user
    }
}
