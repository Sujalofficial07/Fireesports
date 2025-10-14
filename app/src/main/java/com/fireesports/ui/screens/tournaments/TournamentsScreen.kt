package com.fireesports.ui.screens.tournaments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fireesports.navigation.Screen
import com.fireesports.ui.screens.home.TournamentCard
import com.fireesports.ui.theme.*
import com.fireesports.viewmodel.TournamentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentsScreen(
    navController: NavController,
    viewModel: TournamentViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val tournaments by viewModel.tournaments.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "TOURNAMENTS",
            fontFamily = Orbitron,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = NeonPink
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchTournaments(it)
            },
            placeholder = { Text("Search tournaments...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tournaments) { tournament ->
                TournamentCard(
                    tournament = tournament,
                    onClick = { navController.navigate(Screen.TournamentDetail.createRoute(tournament.id)) }
                )
            }
        }
    }
}
