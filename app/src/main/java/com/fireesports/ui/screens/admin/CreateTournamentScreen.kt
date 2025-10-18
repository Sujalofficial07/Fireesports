package com.fireesports.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.data.model.Tournament
import com.fireesports.data.model.TournamentStatus
import com.fireesports.ui.components.GlowButton
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.AdminViewModel
import com.fireesports.viewmodel.AdminUiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTournamentScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var game by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rules by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var entryFee by remember { mutableStateOf("0") }
    var prizePool by remember { mutableStateOf("0") }
    var maxParticipants by remember { mutableStateOf("100") }
    var teamSize by remember { mutableStateOf("1") }
    var startDate by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AdminUiState.TournamentCreated) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Tournament", fontFamily = Orbitron) },
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
                text = "NEW TOURNAMENT",
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = NeonAqua
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tournament Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = game,
                onValueChange = { game = it },
                label = { Text("Game Name") },
                placeholder = { Text("e.g., PUBG Mobile, Free Fire") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            OutlinedTextField(
                value = rules,
                onValueChange = { rules = it },
                label = { Text("Rules") },
                placeholder = { Text("Enter rules (one per line)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                maxLines = 10
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = entryFee,
                    onValueChange = { entryFee = it },
                    label = { Text("Entry Fee (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = prizePool,
                    onValueChange = { prizePool = it },
                    label = { Text("Prize Pool (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = maxParticipants,
                    onValueChange = { maxParticipants = it },
                    label = { Text("Max Players") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = teamSize,
                    onValueChange = { teamSize = it },
                    label = { Text("Team Size") },
                    placeholder = { Text("1=Solo, 4=Squad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("Start Date") },
                placeholder = { Text("Days from now (e.g., 3)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            GlowButton(
                text = if (uiState is AdminUiState.Loading) "CREATING..." else "CREATE TOURNAMENT",
                onClick = {
                    val daysFromNow = startDate.toLongOrNull() ?: 3L
                    val startTime = System.currentTimeMillis() + (daysFromNow * 24 * 60 * 60 * 1000)
                    
                    viewModel.createTournament(
                        title = title,
                        game = game,
                        description = description,
                        rules = rules,
                        imageUrl = imageUrl.ifEmpty { "https://images.unsplash.com/photo-1542751371-adc38448a05e" },
                        entryFee = entryFee.toDoubleOrNull() ?: 0.0,
                        prizePool = prizePool.toDoubleOrNull() ?: 0.0,
                        maxParticipants = maxParticipants.toIntOrNull() ?: 100,
                        teamSize = teamSize.toIntOrNull() ?: 1,
                        startTime = startTime
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonAqua,
                enabled = uiState !is AdminUiState.Loading && 
                         title.isNotBlank() && 
                         game.isNotBlank()
            )

            if (uiState is AdminUiState.Error) {
                Text(
                    text = (uiState as AdminUiState.Error).message,
                    color = Error,
                    fontSize = 14.sp
                )
            }
        }
    }
}
