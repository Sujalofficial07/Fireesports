package com.fireesports.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TournamentStatus
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTournamentsScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val tournaments by viewModel.tournaments.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllTournaments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Tournaments", fontFamily = Orbitron) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkBackground, DarkSurface)
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "ALL TOURNAMENTS",
                    fontFamily = Orbitron,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (tournaments.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tournaments yet", color = TextSecondary)
                    }
                }
            }

            items(tournaments) { tournament ->
                TournamentManageCard(
                    tournament = tournament,
                    onStatusChange = { newStatus ->
                        viewModel.updateTournamentStatus(tournament.id, newStatus)
                    },
                    onDelete = {
                        viewModel.deleteTournament(tournament.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentManageCard(
    tournament: Tournament,
    onStatusChange: (TournamentStatus) -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    NeonCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = when (tournament.status) {
            TournamentStatus.REGISTRATION_OPEN -> Success
            TournamentStatus.LIVE -> Error
            TournamentStatus.UPCOMING -> Warning
            else -> TextSecondary
        }
    ) {
        Column {
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
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = tournament.game,
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = NeonAqua
                    )
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, null, tint = TextPrimary)
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Open Registration") },
                            onClick = {
                                onStatusChange(TournamentStatus.REGISTRATION_OPEN)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Set Upcoming") },
                            onClick = {
                                onStatusChange(TournamentStatus.UPCOMING)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Start (Live)") },
                            onClick = {
                                onStatusChange(TournamentStatus.LIVE)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Complete") },
                            onClick = {
                                onStatusChange(TournamentStatus.COMPLETED)
                                showMenu = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Delete", color = Error) },
                            onClick = {
                                showDeleteDialog = true
                                showMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("Status", tournament.status.name)
                InfoChip("Players", "${tournament.currentParticipants}/${tournament.maxParticipants}")
                InfoChip("Prize", "₹${tournament.prizePool}")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start: ${SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(tournament.startTime))}",
                fontFamily = Montserrat,
                fontSize = 11.sp,
                color = TextTertiary
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Tournament?") },
            text = { Text("This action cannot be undone. All participants will be notified.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
