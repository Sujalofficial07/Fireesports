package com.fireesports.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object ProfileSetup : Screen("profile_setup_screen")
    object Home : Screen("home_screen")
    object Tournaments : Screen("tournaments_screen")
    object Wallet : Screen("wallet_screen")
    object Community : Screen("community_screen")
    object Profile : Screen("profile_screen")
    object Chat : Screen("chat_screen/{roomId}") {
        fun createRoute(roomId: String) = "chat_screen/$roomId"
    }
}
