package com.fireesports.data.repository

import com.fireesports.data.model.ChatMessage
import com.fireesports.data.model.ChatRoom
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val supabaseProvider: SupabaseClientProvider
) {
    private val supabase = supabaseProvider.client

    suspend fun sendMessage(message: ChatMessage): Result<Unit> {
        return try {
            supabase.from("chat_messages").insert(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessages(chatRoomId: String, limit: Int = 50): Result<List<ChatMessage>> {
        return try {
            val messages = supabase.from("chat_messages")
                .select {
                    filter { eq("chatRoomId", chatRoomId) }
                    order(column = "timestamp", ascending = false)
                    limit(limit.toLong())
                }.decodeList<ChatMessage>()
            Result.success(messages.reversed())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun subscribeToMessages(chatRoomId: String): Flow<ChatMessage> {
        val channel = supabase.realtime.channel("chat_$chatRoomId")
        
        return channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "chat_messages"
            filter = "chatRoomId=eq.$chatRoomId"
        }.map { it.record as ChatMessage }
    }

    suspend fun getChatRooms(userId: String): Result<List<ChatRoom>> {
        return try {
            val rooms = supabase.from("chat_rooms")
                .select {
                    filter { contains("participants", listOf(userId)) }
                }.decodeList<ChatRoom>()
            Result.success(rooms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
