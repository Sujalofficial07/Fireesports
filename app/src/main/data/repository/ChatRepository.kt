package com.fireesports.data.repository

import com.fireesports.data.model.ChatMessage
import com.fireesports.data.model.ChatRoom
import com.fireesports.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.createChannel
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
            supabase.postgrest["chat_messages"].insert(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessages(chatRoomId: String, limit: Int = 50): Result<List<ChatMessage>> {
        return try {
            val messages = supabase.postgrest["chat_messages"]
                .select() {
                    filter { eq("chatRoomId", chatRoomId) }
                    order("timestamp", ascending = false)
                    limit(limit.toLong())
                }.decodeList<ChatMessage>()
            Result.success(messages.reversed())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun subscribeToMessages(chatRoomId: String): Flow<ChatMessage> {
        val channel = supabase.realtime.createChannel("chat_$chatRoomId")
        
        return channel.postgresChangeFlow<ChatMessage>(schema = "public") {
            table = "chat_messages"
            filter = "chatRoomId=eq.$chatRoomId"
        }.map { it.record }
    }

    suspend fun getChatRooms(userId: String): Result<List<ChatRoom>> {
        return try {
            val rooms = supabase.postgrest["chat_rooms"]
                .select() {
                    filter { contains("participants", listOf(userId)) }
                }.decodeList<ChatRoom>()
            Result.success(rooms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
