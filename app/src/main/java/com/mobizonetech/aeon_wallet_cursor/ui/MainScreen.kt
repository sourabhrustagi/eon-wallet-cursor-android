package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    userName: String,
    onLogout: () -> Unit
) {
    var currentTab by remember { mutableStateOf("home") }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tab Content
        when (currentTab) {
            "home" -> {
                HomeScreen(userName = userName)
            }
            "scan" -> {
                ScanAndPayScreen()
            }
            "profile" -> {
                ProfileScreen(
                    userName = userName,
                    onLogout = onLogout
                )
            }
        }
        
        // Bottom Navigation (positioned at bottom)
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    }
}
