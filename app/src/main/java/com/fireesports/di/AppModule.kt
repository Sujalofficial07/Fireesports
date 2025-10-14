package com.fireesports.di

import com.fireesports.data.remote.SupabaseClientProvider
import com.fireesports.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClientProvider {
        return SupabaseClientProvider()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseProvider: SupabaseClientProvider
    ): AuthRepository {
        return AuthRepository(supabaseProvider)
    }

    @Provides
    @Singleton
    fun provideTournamentRepository(
        supabaseProvider: SupabaseClientProvider
    ): TournamentRepository {
        return TournamentRepository(supabaseProvider)
    }

    @Provides
    @Singleton
    fun provideWalletRepository(
        supabaseProvider: SupabaseClientProvider
    ): WalletRepository {
        return WalletRepository(supabaseProvider)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        supabaseProvider: SupabaseClientProvider
    ): ChatRepository {
        return ChatRepository(supabaseProvider)
    }
}
