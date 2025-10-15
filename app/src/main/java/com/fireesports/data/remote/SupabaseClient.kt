package com.fireesports.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClientProvider @Inject constructor() {
    
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://datrazwjswjmvpephmzo.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRhdHJhendqc3dqbXZwZXBobXpvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAzODQ5NzcsImV4cCI6MjA3NTk2MDk3N30.eOWIovkbmMX71-mXPi3OfKralTKCgRv4WqX4bao6j3I"
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}
