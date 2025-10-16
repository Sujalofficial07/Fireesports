package com.fireesports.data.repository

import com.fireesports.data.model.User
import com.fireesports.data.model.UserRole
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
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
            
            // Fetch user profile
            val users = supabase.from("users")
                .select {
                    filter { 
                        eq("id", userId) 
                    }
                }
                .decodeList<User>()
            
            val user = users.firstOrNull() ?: throw Exception("User profile not found")
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
            
            supabase.from("users").insert(user)
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(user: User): Result<User> {
        return try {
            val updates = mapOf(
                "username" to user.username,
                "gamerId" to user.gamerId,
                "avatarUrl" to user.avatarUrl,
                "phoneNumber" to user.phoneNumber,
                "language" to user.language,
                "updatedAt" to System.currentTimeMillis()
            )
            
            supabase.from("users").update(updates) {
                filter { 
                    eq("id", user.id) 
                }
            }
            
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUserProfile(): Result<User?> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId == null) {
                _currentUser.value = null
                return Result.success(null)
            }
            
            val users = supabase.from("users")
                .select {
                    filter { 
                        eq("id", userId) 
                    }
                }
                .decodeList<User>()
            
            val user = users.firstOrNull()
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
