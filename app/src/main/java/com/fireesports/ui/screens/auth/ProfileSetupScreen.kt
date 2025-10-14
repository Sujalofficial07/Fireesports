package com.fireesports.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Gamepad
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
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AuthUiState
import com.fireesports.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var gamerId by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.ProfileCreated -> navController.navigate(Screen.Home.route) {
                popUpTo(Screen.ProfileSetup.route) { inclusive = true }
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SETUP PROFILE",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = NeonPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Complete your gaming profile",
                fontFamily = Montserrat,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                placeholder = { Text("Enter your username") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = DarkBorder,
                    focusedLabelColor = NeonPurple,
                    cursorColor = NeonPurple
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = gamerId,
                onValueChange = { gamerId = it },
                label = { Text("Gamer ID") },
                placeholder = { Text("Your in-game ID") },
                leadingIcon = { Icon(Icons.Default.Gamepad, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = DarkBorder,
                    focusedLabelColor = NeonPurple,
                    cursorColor = NeonPurple
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is the ID you use in games like PUBG, Free Fire, etc.",
                fontFamily = Montserrat,
                fontSize = 12.sp,
                color = TextTertiary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlowButton(
                text = if (uiState is AuthUiState.Loading) "CREATING..." else "COMPLETE SETUP",
                onClick = { viewModel.createProfile(username, gamerId) },
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPurple,
                enabled = uiState !is AuthUiState.Loading && username.isNotBlank() && gamerId.isNotBlank()
            )

            if (uiState is AuthUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = Error,
                    fontSize = 14.sp,
                    fontFamily = Montserrat
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🎮 Join the gaming revolution",
                fontFamily = Montserrat,
                fontSize = 12.sp,
                color = TextTertiary
            )
        }
    }
}
