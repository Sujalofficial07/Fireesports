package com.fireesports.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fireesports.ui.screens.admin.AdminPanelScreen
import com.fireesports.ui.screens.admin.AnalyticsScreen
import com.fireesports.ui.screens.admin.CreateTournamentScreen
import com.fireesports.ui.screens.admin.ManageTournamentsScreen
import com.fireesports.ui.screens.admin.ManageUsersScreen
import com.fireesports.ui.screens.auth.LoginScreen
import com.fireesports.ui.screens.auth.ProfileSetupScreen
import com.fireesports.ui.screens.auth.RegisterScreen
import com.fireesports.ui.screens.community.ChatScreen
import com.fireesports.ui.screens.community.CommunityScreen
import com.fireesports.ui.screens.home.HomeScreen
import com.fireesports.ui.screens.profile.ProfileScreen
import com.fireesports.ui.screens.tournaments.TournamentDetailScreen
import com.fireesports.ui.screens.tournaments.TournamentsScreen
import com.fireesports.ui.screens.wallet.WalletScreen
import com.fireesports.viewmodel.AuthViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route

    Scaffold(
        bottomBar = {
            if (currentUser != null && shouldShowBottomBar(navController)) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Auth screens
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Register.route) { RegisterScreen(navController) }
            composable(Screen.ProfileSetup.route) { ProfileSetupScreen(navController) }

            // Main screens
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Tournaments.route) { TournamentsScreen(navController) }
            composable(
                route = Screen.TournamentDetail.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tournamentId = backStackEntry.arguments?.getString("tournamentId") ?: ""
                TournamentDetailScreen(navController, tournamentId)
            }
            composable(Screen.Wallet.route) { WalletScreen(navController) }
            composable(Screen.Community.route) { CommunityScreen(navController) }
            composable(
                route = Screen.Chat.route,
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatScreen(navController, chatId)
            }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            
            // Admin screens
            composable(Screen.AdminPanel.route) { AdminPanelScreen(navController) }
            composable(Screen.CreateTournament.route) { CreateTournamentScreen(navController) }
            composable(Screen.ManageTournaments.route) { ManageTournamentsScreen(navController) }
            composable(Screen.ManageUsers.route) { ManageUsersScreen(navController) }
            composable(Screen.Analytics.route) { AnalyticsScreen(navController) }
        }
    }
}

@Composable
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute in listOf(
        Screen.Home.route,
        Screen.Tournaments.route,
        Screen.Wallet.route,
        Screen.Community.route,
        Screen.Profile.route
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Tournaments", Screen.Tournaments.route, Icons.Default.EmojiEvents),
        BottomNavItem("Wallet", Screen.Wallet.route, Icons.Default.AccountBalanceWallet),
        BottomNavItem("Community", Screen.Community.route, Icons.Default.People),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
