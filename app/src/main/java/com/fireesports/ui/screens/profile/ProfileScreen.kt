package com.fireesports.ui.screens.profile

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
import com.fireesports.navigation.Screen
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.components.NeonCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "PROFILE",
            fontFamily = Orbitron,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = NeonPurple
        )

        Spacer(modifier = Modifier.height(24.dp))

        currentUser?.let { user ->
            NeonCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAqua
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = NeonAqua,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.username,
                        fontFamily = Orbitron,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = user.gamerId,
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Level ${user.level}",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Warning
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonPink
            ) {
                ProfileInfoRow("Email", user.email)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoRow("XP Points", "${user.xp}")
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoRow("Wallet Balance", "₹${String.format("%.2f", user.walletBalance)}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlowButton(
                text = "EDIT PROFILE",
                onClick = { showEditDialog = true },
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Error
                )
            ) {
                Text("SIGN OUT", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentUser = currentUser,
            onDismiss = { showEditDialog = false },
            onSave = { user ->
                viewModel.updateProfile(user)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentUser: com.fireesports.data.model.User?,
    onDismiss: () -> Unit,
    onSave: (com.fireesports.data.model.User) -> Unit
) {
    var username by remember { mutableStateOf(currentUser?.username ?: "") }
    var gamerId by remember { mutableStateOf(currentUser?.gamerId ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profile",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = gamerId,
                    onValueChange = { gamerId = it },
                    label = { Text("Gamer ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    currentUser?.let {
                        onSave(it.copy(username = username, gamerId = gamerId))
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
