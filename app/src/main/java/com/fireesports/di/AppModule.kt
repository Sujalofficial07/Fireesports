package com.fireesports.di

import android.content.Context
import com.fireesports.data.remote.SupabaseClientProvider
import com.fireesports.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        supabaseProvider: SupabaseClientProvider,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(supabaseProvider, context)
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

    @Provides
    @Singleton
    fun provideAdminRepository(
        supabaseProvider: SupabaseClientProvider
    ): AdminRepository {
        return AdminRepository(supabaseProvider)
    }
}
