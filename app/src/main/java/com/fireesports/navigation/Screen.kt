package com.fireesports.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object Tournaments : Screen("tournaments")
    object TournamentDetail : Screen("tournament/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament/$tournamentId"
    }
    object Wallet : Screen("wallet")
    object Community : Screen("community")
    object Chat : Screen("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
