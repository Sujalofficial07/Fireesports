package com.fireesports.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val tournaments by viewModel.tournaments.collectAsState()
    val users by viewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllTournaments()
        viewModel.loadAllUsers()
    }

    // Calculate stats
    val totalTournaments = tournaments.size
    val activeTournaments = tournaments.count { it.status.name in listOf("REGISTRATION_OPEN", "LIVE") }
    val totalUsers = users.size
    val totalRevenue = tournaments.sumOf { it.entryFee * it.currentParticipants }
    val totalPrizePool = tournaments.filter { it.status.name != "CANCELLED" }.sumOf { it.prizePool }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontFamily = Orbitron) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { paddingValues ->
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PLATFORM ANALYTICS",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = NeonPurple
            )

            // Key Metrics
            Text(
                text = "Key Metrics",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Total Users",
                    value = totalUsers.toString(),
                    icon = Icons.Default.People,
                    color = NeonAqua,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Tournaments",
                    value = totalTournaments.toString(),
                    icon = Icons.Default.EmojiEvents,
                    color = NeonPink,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Active Now",
                    value = activeTournaments.toString(),
                    icon = Icons.Default.Whatshot,
                    color = Success,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Total Revenue",
                    value = "₹${String.format("%.0f", totalRevenue)}",
                    icon = Icons.Default.AccountBalance,
                    color = Warning,
                    modifier = Modifier.weight(1f)
                )
            }

            // Tournament Breakdown
            Text(
                text = "Tournament Status",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )

            NeonCard(borderColor = NeonAqua) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusRow("Registration Open", tournaments.count { it.status.name == "REGISTRATION_OPEN" }, Success)
                    StatusRow("Upcoming", tournaments.count { it.status.name == "UPCOMING" }, Warning)
                    StatusRow("Live", tournaments.count { it.status.name == "LIVE" }, Error)
                    StatusRow("Completed", tournaments.count { it.status.name == "COMPLETED" }, TextSecondary)
                }
            }

            // Financial Overview
            Text(
                text = "Financial Overview",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )

            NeonCard(borderColor = Success) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FinancialRow("Total Revenue", totalRevenue)
                    FinancialRow("Total Prize Pool", totalPrizePool)
                    FinancialRow("Net Income", totalRevenue - totalPrizePool)
                }
            }

            // User Stats
            Text(
                text = "User Statistics",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Admins",
                    value = users.count { it.role.name == "ADMIN" }.toString(),
                    icon = Icons.Default.AdminPanelSettings,
                    color = NeonPurple,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Players",
                    value = users.count { it.role.name == "PLAYER" }.toString(),
                    icon = Icons.Default.SportsEsports,
                    color = NeonAqua,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    NeonCard(
        modifier = modifier.height(120.dp),
        borderColor = color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = color
            )
            Text(
                text = title,
                fontFamily = Montserrat,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StatusRow(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            color = TextPrimary
        )
        Text(
            text = count.toString(),
            fontFamily = Orbitron,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = color
        )
    }
}

@Composable
fun FinancialRow(label: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            color = TextPrimary
        )
        Text(
            text = "₹${String.format("%.2f", amount)}",
            fontFamily = Orbitron,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Success
        )
    }
}
