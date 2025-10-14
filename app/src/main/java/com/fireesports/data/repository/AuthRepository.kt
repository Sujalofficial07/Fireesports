package com.fireesports.data.repository

import com.fireesports.data.model.User
import com.fireesports.data.model.UserRole
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
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

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            
            val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No user found")
            val user = supabase.postgrest["users"]
                .select() {
                    filter { eq("id", userId) }
                }.decodeSingle<User>()
            
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            _currentUser.value = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserProfile(username: String, gamerId: String): Result<User> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No user found")
            val email = supabase.auth.currentUserOrNull()?.email ?: ""
            
            val user = User(
                id = userId,
                email = email,
                username = username,
                gamerId = gamerId,
                role = UserRole.PLAYER
            )
            
            supabase.postgrest["users"].insert(user)
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(user: User): Result<User> {
        return try {
            supabase.postgrest["users"].update({
                set("username", user.username)
                set("gamerId", user.gamerId)
                set("avatarUrl", user.avatarUrl)
                set("phoneNumber", user.phoneNumber)
                set("language", user.language)
                set("updatedAt", System.currentTimeMillis())
            }) {
                filter { eq("id", user.id) }
            }
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
