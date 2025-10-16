package com.fireesports.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fireesports.ui.theme.*
import com.fireesports.ui.components.GlowButton

@Composable
fun WalletScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackground, DarkSurface))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "YOUR WALLET",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = NeonGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Current Balance: ₹0.00",
                fontFamily = Montserrat,
                fontSize = 18.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlowButton(
                text = "ADD FUNDS (Coming Soon)",
                onClick = { },
                modifier = Modifier.padding(horizontal = 32.dp),
                glowColor = NeonGreen,
                enabled = false
            )
        }
    }
}
