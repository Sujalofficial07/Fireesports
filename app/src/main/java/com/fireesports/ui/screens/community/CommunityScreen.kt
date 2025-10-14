package com.fireesports.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.data.model.ChatRoom
import com.fireesports.data.model.ChatType
import com.fireesports.navigation.Screen
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.CommunityViewModel
import com.fireesports.viewmodel.CommunityUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val chatRooms by viewModel.chatRooms.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "COMMUNITY",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = NeonAqua
            )
            
            IconButton(onClick = { /* Add new chat functionality */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Chat",
                    tint = NeonPink,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionChip(
                icon = Icons.Default.Public,
                label = "Global",
                color = NeonAqua,
                onClick = { /* Filter global chats */ },
                modifier = Modifier.weight(1f)
            )
            QuickActionChip(
                icon = Icons.Default.Group,
                label = "Teams",
                color = NeonPink,
                onClick = { /* Filter team chats */ },
                modifier = Modifier.weight(1f)
            )
            QuickActionChip(
                icon = Icons.Default.Person,
                label = "Private",
                color = NeonPurple,
                onClick = { /* Filter private chats */ },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Chat Rooms Section
        when (uiState) {
            is CommunityUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonAqua)
                }
            }
            is CommunityUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (uiState as CommunityUiState.Error).message,
                            color = Error,
                            fontFamily = Montserrat,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            is CommunityUiState.Success -> {
                if (chatRooms.isEmpty()) {
                    // Empty State
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No Chats Yet",
                                fontFamily = Orbitron,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Join a tournament or start chatting with other players",
                                fontFamily = Montserrat,
                                fontSize = 14.sp,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            GlowButton(
                                text = "EXPLORE TOURNAMENTS",
                                onClick = { navController.navigate(Screen.Tournaments.route) },
                                modifier = Modifier.fillMaxWidth(),
                                glowColor = NeonPink
                            )
                        }
                    }
                } else {
                    // Chat Rooms List
                    Column {
                        Text(
                            text = "ACTIVE CHATS",
                            fontFamily = Orbitron,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(chatRooms) { chatRoom ->
                                ChatRoomCard(
                                    chatRoom = chatRoom,
                                    onClick = { navController.navigate(Screen.Chat.createRoute(chatRoom.id)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeonCard(
        modifier = modifier
            .height(60.dp)
            .clickable(onClick = onClick),
        borderColor = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun ChatRoomCard(chatRoom: ChatRoom, onClick: () -> Unit) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = when (chatRoom.type) {
            ChatType.GLOBAL -> NeonAqua
            ChatType.TEAM -> NeonPink
            ChatType.PRIVATE -> NeonPurple
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Chat Icon/Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when (chatRoom.type) {
                                ChatType.GLOBAL -> NeonAqua.copy(alpha = 0.2f)
                                ChatType.TEAM -> NeonPink.copy(alpha = 0.2f)
                                ChatType.PRIVATE -> NeonPurple.copy(alpha = 0.2f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (chatRoom.type) {
                            ChatType.GLOBAL -> Icons.Default.Public
                            ChatType.TEAM -> Icons.Default.Group
                            ChatType.PRIVATE -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = when (chatRoom.type) {
                            ChatType.GLOBAL -> NeonAqua
                            ChatType.TEAM -> NeonPink
                            ChatType.PRIVATE -> NeonPurple
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = chatRoom.name,
                            fontFamily = Orbitron,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        
                        if (chatRoom.type == ChatType.GLOBAL) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = Success,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    chatRoom.lastMessage?.let { lastMsg ->
                        Text(
                            text = lastMsg,
                            fontFamily = Montserrat,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    } ?: run {
                        Text(
                            text = "No messages yet",
                            fontFamily = Montserrat,
                            fontSize = 12.sp,
                            color = TextTertiary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Participants count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${chatRoom.participants.size} members",
                            fontFamily = Montserrat,
                            fontSize = 10.sp,
                            color = TextTertiary
                        )
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (chatRoom.lastMessageTime > 0) {
                    Text(
                        text = formatTime(chatRoom.lastMessageTime),
                        fontFamily = Montserrat,
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }
                
                // Unread badge (placeholder)
                if (chatRoom.lastMessage != null && Math.random() > 0.5) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Error),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(1..9).random()}",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
}
