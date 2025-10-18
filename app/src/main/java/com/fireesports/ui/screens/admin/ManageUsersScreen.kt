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
import com.fireesports.data.model.User
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    val filteredUsers = users.filter {
        it.username.contains(searchQuery, ignoreCase = true) ||
        it.email.contains(searchQuery, ignoreCase = true) ||
        it.gamerId.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Users", fontFamily = Orbitron) },
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
        ) {
            Text(
                text = "ALL USERS (${users.size})",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search users...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredUsers) { user ->
                    UserCard(user)
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User) {
    NeonCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = when (user.role.name) {
            "ADMIN" -> NeonPurple
            "MODERATOR" -> NeonPink
            else -> NeonAqua
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
                        text = user.username,
                        fontFamily = Orbitron,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = user.gamerId,
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = NeonAqua
                    )
                    Text(
                        text = user.email,
                        fontFamily = Montserrat,
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = user.role.name,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = when (user.role.name) {
                            "ADMIN" -> NeonPurple
                            "MODERATOR" -> NeonPink
                            else -> Success
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lvl ${user.level}",
                        fontFamily = Montserrat,
                        fontSize = 11.sp,
                        color = Warning
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("XP", "${user.xp}")
                InfoChip("Wallet", "₹${String.format("%.2f", user.walletBalance)}")
                InfoChip("Status", if (user.isOnline) "Online" else "Offline")
            }
        }
    }
}
