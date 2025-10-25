package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf(
        Triple("home", "Home", Icons.Default.Home),
        Triple("scan", "Scan & Pay", Icons.Default.Add),
        Triple("profile", "Profile", Icons.Default.Person)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEach { (tabId, tabName, icon) ->
                val isSelected = currentTab == tabId
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { onTabSelected(tabId) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            icon,
                            contentDescription = tabName,
                            tint = if (isSelected) 
                                MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Text(
                        text = tabName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) 
                            MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
