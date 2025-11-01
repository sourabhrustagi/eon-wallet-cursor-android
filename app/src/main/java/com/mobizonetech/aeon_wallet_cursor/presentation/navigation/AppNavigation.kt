package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.LoginScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.WelcomeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.HomeScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Login : Screen("login")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(Screen.Home.route) },
                onSignIn = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) {
                    // Clear back stack to prevent going back to login
                    popUpTo(Screen.Login.route) { inclusive = true }
                } }
            )
        }
    }
}


