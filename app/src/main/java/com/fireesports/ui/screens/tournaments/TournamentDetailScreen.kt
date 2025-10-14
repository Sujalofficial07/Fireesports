package com.fireesports.ui.screens.tournaments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.TournamentUiState
import com.fireesports.viewmodel.TournamentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    navController: NavController,
    tournamentId: String,
    viewModel: TournamentViewModel = hiltViewModel()
) {
    val tournament by viewModel.selectedTournament.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tournamentId) {
        viewModel.loadTournamentDetails(tournamentId)
    }

    LaunchedEffect(uiState) {
        if (uiState is TournamentUiState.JoinSuccess) {
            // Show success message
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournament Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        tournament?.let { t ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkBackground, DarkSurface)
                        )
                    )
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = t.title,
                    fontFamily = Orbitron,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = NeonAqua
                )

                Spacer(modifier = Modifier.height(16.dp))

                NeonCard(borderColor = NeonPink) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            label = "Prize Pool",
                            value = "₹${t.prizePool}",
                            icon = Icons.Default.EmojiEvents
                        )
                        InfoItem(
                            label = "Participants",
                            value = "${t.currentParticipants}/${t.maxParticipants}",
                            icon = Icons.Default.People
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonCard(borderColor = NeonPurple) {
                    Column {
                        Text(
                            text = "Game",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = t.game,
                            fontFamily = Orbitron,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Start Time",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(t.startTime)),
                            fontFamily = Montserrat,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonCard(borderColor = NeonAqua) {
                    Text(
                        text = "Description",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = t.description,
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonCard(borderColor = Warning) {
                    Text(
                        text = "Rules",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = t.rules,
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                GlowButton(
                    text = when (uiState) {
                        is TournamentUiState.Joining -> "JOINING..."
                        else -> if (t.entryFee > 0) "JOIN (₹${t.entryFee})" else "JOIN FREE"
                    },
                    onClick = { viewModel.joinTournament(tournamentId) },
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = Success,
                    enabled = uiState !is TournamentUiState.Joining
                )

                if (uiState is TournamentUiState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (uiState as TournamentUiState.Error).message,
                        color = Error,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = NeonAqua, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = Orbitron,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextPrimary
        )
    }
}
