package com.mobizonetech.aeon_wallet_cursor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.data.UserData
import com.mobizonetech.aeon_wallet_cursor.data.UserPreferences
import com.mobizonetech.aeon_wallet_cursor.utils.RandomNameGenerator
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.*
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.loans.LoanCardApplicationScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.common.MainScreen
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var userPreferences: UserPreferences
    
    @Inject
    lateinit var unlockPreferences: UnlockPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            AeonwalletcursorTheme {
                AeonWalletApp(
                    userPreferences = userPreferences,
                    unlockPreferences = unlockPreferences
                )
            }
        }
    }
}

@Composable
fun AeonWalletApp(
    userPreferences: UserPreferences,
    unlockPreferences: UnlockPreferences
) {
    var currentScreen by remember { mutableStateOf("loading") }
    var userData by remember { mutableStateOf(UserData()) }
    var displayName by remember { mutableStateOf(RandomNameGenerator.generateRandomName()) }
    val coroutineScope = rememberCoroutineScope()
    
    // Check login state on startup
    LaunchedEffect(Unit) {
        userPreferences.isLoggedIn.collect { isLoggedIn ->
            if (isLoggedIn) {
                userPreferences.userData.collect { savedUserData ->
                    userData = savedUserData
                    currentScreen = "home"
                }
            } else {
                currentScreen = "welcome"
            }
        }
    }
    
    when (currentScreen) {
        "loading" -> {
            // Show loading screen while checking login state
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Text(
                    text = "Loading...",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
        "welcome" -> {
            WelcomeScreen(
                onGetStartedClick = { currentScreen = "enterId" },
                onSignInClick = { currentScreen = "enterId" },
                onApplyForLoanCardClick = { currentScreen = "loanCardApplication" }
            )
        }
        "enterId" -> {
            EnterIdScreen(
                onBackClick = { currentScreen = "welcome" },
                onContinueClick = { id, cardNumber, cardType ->
                    userData = userData.copy(id = id, cardNumber = cardNumber, cardType = cardType)
                    currentScreen = "otp"
                }
            )
        }
        "otp" -> {
            OtpScreen(
                phoneNumber = "+1 234 567 8900", // This would come from user data
                onBackClick = { currentScreen = "enterId" },
                onOtpVerified = { currentScreen = "passcode" }
            )
        }
        "passcode" -> {
            SetPasscodeScreen(
                onBackClick = { currentScreen = "otp" },
                onPasscodeSet = { passcode ->
                    userData = userData.copy(passcode = passcode)
                    currentScreen = "securityPasscode"
                }
            )
        }
        "securityPasscode" -> {
            SetSecurityPasscodeScreen(
                onBackClick = { currentScreen = "passcode" },
                onSecurityPasscodeSet = { securityPasscode ->
                    userData = userData.copy(securityPasscode = securityPasscode)
                    // Save login state when user completes onboarding
                    coroutineScope.launch {
                        userPreferences.setLoggedIn(userData)
                    }
                    currentScreen = "home"
                }
            )
        }
        "loanCardApplication" -> {
            LoanCardApplicationScreen(
                onBackClick = { currentScreen = "welcome" },
                onApplicationSubmitted = { currentScreen = "welcome" }
            )
        }
        "home" -> {
            MainScreen(
                userName = displayName,
                onLogout = {
                    // Handle logout - reset unlock states and navigate to welcome screen
                    coroutineScope.launch {
                        unlockPreferences.resetAllUnlocks()
                        userPreferences.logout()
                    }
                    // Generate a new random name for next login
                    displayName = RandomNameGenerator.generateRandomName()
                    currentScreen = "welcome"
                }
            )
        }
    }
}