package com.fireesports.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fireesports.navigation.Screen
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AuthUiState
import com.fireesports.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.SignUpSuccess -> {
                navController.navigate(Screen.ProfileSetup.route)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackground, DarkSurface)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "REGISTER",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = NeonPink
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPink,
                    unfocusedBorderColor = DarkBorder,
                    focusedLabelColor = NeonPink,
                    cursorColor = NeonPink
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPink,
                    unfocusedBorderColor = DarkBorder,
                    focusedLabelColor = NeonPink,
                    cursorColor = NeonPink
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlowButton(
                text = if (uiState is AuthUiState.Loading) "REGISTERING..." else "REGISTER",
                onClick = { viewModel.signUp(email, password) },
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink,
                enabled = uiState !is AuthUiState.Loading &&
                          email.isNotBlank() &&
                          password.isNotBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text("Already have an account? Login", color = NeonPink)
            }

            if (uiState is AuthUiState.Error) {
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = Error,
                    fontSize = 14.sp,
                    fontFamily = Montserrat
                )
            }
        }
    }
}
