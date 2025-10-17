package com.fireesports.data.repository

import android.content.Context
import com.fireesports.data.model.User
import com.fireesports.data.model.UserRole
import com.fireesports.data.remote.SupabaseClientProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider,
    @ApplicationContext private val context: Context
) {
    private val supabase = supabaseProvider.client
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Google Sign-In client
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID) // Replace with your Web Client ID
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

    // Email/Password Sign Up
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

    // Email/Password Sign In
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
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
            
            val user = users.firstOrNull() ?: throw Exception("User profile not found")
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Google Sign In
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<User> {
        return try {
            val idToken = account.idToken ?: throw Exception("No ID token")
            
            // Sign in to Supabase with Google ID token
            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            
            val userId = supabase.auth.currentUserOrNull()?.id ?: throw Exception("No user found")
            val email = account.email ?: ""
            val displayName = account.displayName ?: ""
            val photoUrl = account.photoUrl?.toString()
            
            // Check if user profile exists
            val existingUsers = supabase.from("users")
                .select {
                    filter { eq("id", userId) }
                }
                .decodeList<User>()
            
            val user = if (existingUsers.isEmpty()) {
                // Create new profile for Google user
                val newUser = User(
                    id = userId,
                    email = email,
                    username = displayName.ifEmpty { email.substringBefore("@") },
                    gamerId = "G${userId.take(8)}", // Auto-generate gamer ID
                    avatarUrl = photoUrl,
                    role = UserRole.PLAYER
                )
                supabase.from("users").insert(newUser)
                newUser
            } else {
                existingUsers.first()
            }
            
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign Out
    suspend fun signOut(): Result<Unit> {
        return try {
            supabase.auth.signOut()
            googleSignInClient.signOut()
            _currentUser.value = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Create User Profile
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

    // Update Profile
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
                filter { eq("id", user.id) }
            }
            
            _currentUser.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        // TODO: Replace with your actual Web Client ID from Firebase Console
        private const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
    }
}
