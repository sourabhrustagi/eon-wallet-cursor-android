package com.mobizonetech.aeon_wallet_cursor.presentation.screens.common

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AeonWalletNavigationEnhanced
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AeonWalletDestinations
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AnimatedBottomNavigationBar
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AnimatedFloatingActionButton
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AnimatedTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userName: String = "John Doe",
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    var currentTab by remember { mutableStateOf("home") }
    var currentRoute by remember { mutableStateOf("home") }
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val unlockedCards by unlockPreferences.unlockedCards.collectAsState(initial = emptySet())
    
    Scaffold(
        topBar = {
            AnimatedTopAppBar(
                title = stringResource(R.string.app_name),
                onMenuClick = { /* Handle menu click */ },
                onNotificationClick = { navController.navigate(AeonWalletRoutes.NOTIFICATIONS) }
            )
        },
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                AnimatedBottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
            if (currentRoute == "home") {
                AnimatedFloatingActionButton(
                    onClick = { navController.navigate(AeonWalletDestinations.Scan.route) }
                )
            }
        }
    ) { innerPadding ->
        AeonWalletNavigationEnhanced(
            navController = navController,
            userName = userName,
            onLogout = onLogout
        )
    }
}

private fun shouldShowBottomBar(currentRoute: String): Boolean {
    val hiddenRoutes = listOf(
        AeonWalletDestinations.WebView.route,
        AeonWalletDestinations.LoanDetail.route,
        AeonWalletDestinations.CardDetail.route,
        AeonWalletDestinations.BankProducts.route,
        AeonWalletDestinations.CardApplication.route,
        AeonWalletDestinations.LoanApplication.route,
        AeonWalletDestinations.SendMoney.route,
        AeonWalletDestinations.RequestMoney.route,
        AeonWalletDestinations.Feedback.route,
        AeonWalletDestinations.PrivacyPolicy.route,
        AeonWalletDestinations.TermsConditions.route,
        AeonWalletDestinations.RedeemPoints.route,
        AeonWalletDestinations.Organizations.route,
        AeonWalletDestinations.Donation.route,
        AeonWalletDestinations.CardRepayment.route,
        AeonWalletDestinations.LoanRepayment.route,
        AeonWalletDestinations.CryptoTrading.route,
        AeonWalletDestinations.Settings.route,
        AeonWalletDestinations.ContactUs.route,
        AeonWalletDestinations.UtilityPayment.route
    )
    return currentRoute !in hiddenRoutes
}

}