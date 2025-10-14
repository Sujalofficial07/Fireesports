package com.fireesports.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.data.model.Tournament
import com.fireesports.navigation.Screen
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val featuredTournaments by viewModel.featuredTournaments.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "FIRE eSPORTS",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = NeonAqua
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            QuickActionsRow(navController)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "FEATURED TOURNAMENTS",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(featuredTournaments) { tournament ->
            TournamentCard(
                tournament = tournament,
                onClick = { navController.navigate(Screen.TournamentDetail.createRoute(tournament.id)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            GlowButton(
                text = "VIEW ALL TOURNAMENTS",
                onClick = { navController.navigate(Screen.Tournaments.route) },
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink
            )
        }
    }
}

@Composable
fun QuickActionsRow(navController: NavController) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            QuickActionCard(
                title = "Tournaments",
                icon = Icons.Default.EmojiEvents,
                color = NeonAqua,
                onClick = { navController.navigate(Screen.Tournaments.route) }
            )
        }
        item {
            QuickActionCard(
                title = "Wallet",
                icon = Icons.Default.Wallet,
                color = NeonPink,
                onClick = { navController.navigate(Screen.Wallet.route) }
            )
        }
        item {
            QuickActionCard(
                title = "Community",
                icon = Icons.Default.People,
                color = NeonPurple,
                onClick = { navController.navigate(Screen.Community.route) }
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        borderColor = color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun TournamentCard(tournament: Tournament, onClick: () -> Unit) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = NeonAqua
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tournament.title,
                    fontFamily = Orbitron,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tournament.game,
                    fontFamily = Montserrat,
                    fontSize = 14.sp,
                    color = NeonAqua
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Prize: ₹${tournament.prizePool}",
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = Success
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${tournament.currentParticipants}/${tournament.maxParticipants}",
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = tournament.status.name,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = when (tournament.status.name) {
                        "LIVE" -> Error
                        "UPCOMING" -> Warning
                        else -> Success
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(tournament.startTime)),
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    color = TextTertiary
                )
            }
        }
    }
}
