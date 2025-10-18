package com.fireesports.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.data.model.UserRole
import com.fireesports.navigation.Screen
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    
    // Check if user is admin
    if (currentUser?.role != UserRole.ADMIN) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Error,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Access Denied", color = Error, fontFamily = Orbitron, fontSize = 20.sp)
                Text("Admin privileges required", color = TextSecondary)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Admin Panel",
                        fontFamily = Orbitron,
                        fontWeight = FontWeight.Bold
                    )
                },
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
                    text = "ADMIN DASHBOARD",
                    fontFamily = Orbitron,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = NeonPurple
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Quick Stats
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Tournaments", "12", NeonAqua, Modifier.weight(1f))
                    StatCard("Users", "1,234", NeonPink, Modifier.weight(1f))
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Admin Actions
            val actions = listOf(
                AdminAction("Create Tournament", Icons.Default.Add, NeonAqua, Screen.CreateTournament.route),
                AdminAction("Manage Tournaments", Icons.Default.EmojiEvents, NeonPink, Screen.ManageTournaments.route),
                AdminAction("Manage Users", Icons.Default.People, NeonPurple, Screen.ManageUsers.route),
                AdminAction("Wallet Management", Icons.Default.AccountBalance, Success, Screen.AdminWallet.route),
                AdminAction("Analytics", Icons.Default.Analytics, Warning, Screen.Analytics.route)
            )

            items(actions) { action ->
                AdminActionCard(
                    action = action,
                    onClick = { navController.navigate(action.route) }
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    NeonCard(
        modifier = modifier.height(100.dp),
        borderColor = color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = color
            )
            Text(
                text = label,
                fontFamily = Montserrat,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun AdminActionCard(action: AdminAction, onClick: () -> Unit) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = action.color
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = null,
                tint = action.color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = action.title,
                    fontFamily = Orbitron,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
}

data class AdminAction(
    val title: String,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color,
    val route: String
)
