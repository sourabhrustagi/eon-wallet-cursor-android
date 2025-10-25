package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Account Settings
            item {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(getAccountSettings()) { setting ->
                SettingsItem(
                    setting = setting,
                    onClick = { /* Handle setting click */ }
                )
            }
            
            // Security Settings
            item {
                Text(
                    text = "Security",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            
            items(getSecuritySettings()) { setting ->
                SettingsItem(
                    setting = setting,
                    onClick = { /* Handle setting click */ }
                )
            }
            
            // App Settings
            item {
                Text(
                    text = "App",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            
            items(getAppSettings()) { setting ->
                SettingsItem(
                    setting = setting,
                    onClick = { /* Handle setting click */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SettingsItem(
    setting: SettingItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    setting.icon,
                    contentDescription = null,
                    tint = setting.iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = setting.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    if (setting.subtitle.isNotEmpty()) {
                        Text(
                            text = setting.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class SettingItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val icon: ImageVector,
    val iconColor: Color
)

fun getAccountSettings(): List<SettingItem> = listOf(
    SettingItem(
        id = "profile",
        title = "Profile Information",
        subtitle = "Update your personal details",
        icon = Icons.Default.Person,
        iconColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
    ),
    SettingItem(
        id = "notifications",
        title = "Notification Preferences",
        subtitle = "Manage notification settings",
        icon = Icons.Default.Notifications,
        iconColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
    ),
    SettingItem(
        id = "language",
        title = "Language",
        subtitle = "English",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
    )
)

fun getSecuritySettings(): List<SettingItem> = listOf(
    SettingItem(
        id = "passcode",
        title = "Change Passcode",
        subtitle = "Update your security passcode",
        icon = Icons.Default.Lock,
        iconColor = androidx.compose.ui.graphics.Color(0xFFF44336)
    ),
    SettingItem(
        id = "biometric",
        title = "Biometric Authentication",
        subtitle = "Use fingerprint or face unlock",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFFF44336)
    ),
    SettingItem(
        id = "twoFactor",
        title = "Two-Factor Authentication",
        subtitle = "Add extra security to your account",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFFF44336)
    )
)

fun getAppSettings(): List<SettingItem> = listOf(
    SettingItem(
        id = "theme",
        title = "Theme",
        subtitle = "Light",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
    ),
    SettingItem(
        id = "currency",
        title = "Default Currency",
        subtitle = "USD",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
    ),
    SettingItem(
        id = "dataUsage",
        title = "Data Usage",
        subtitle = "Manage app data",
        icon = Icons.Default.Star,
        iconColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
    ),
    SettingItem(
        id = "about",
        title = "About",
        subtitle = "App version 1.0.0",
        icon = Icons.Default.Info,
        iconColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
    )
)
