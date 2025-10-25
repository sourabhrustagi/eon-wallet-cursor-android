package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    var selectedCategory by remember { mutableStateOf("All") }
    val notifications = getNotifications()
    val filteredNotifications = if (selectedCategory == "All") {
        notifications
    } else {
        notifications.filter { it.category == selectedCategory }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                actions = {
                    IconButton(onClick = { /* Mark all as read */ }) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Mark all as read"
                        )
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Notification Categories
            item {
                NotificationCategoriesSection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            
            // Recent Notifications
            item {
                Text(
                    text = "Recent Notifications",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            items(filteredNotifications) { notification ->
                NotificationCard(notification = notification)
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun NotificationCategoriesSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificationCategoryChip(
            text = "All",
            isSelected = selectedCategory == "All",
            onClick = { onCategorySelected("All") }
        )
        NotificationCategoryChip(
            text = "Transactions",
            isSelected = selectedCategory == "Transactions",
            onClick = { onCategorySelected("Transactions") }
        )
        NotificationCategoryChip(
            text = "Security",
            isSelected = selectedCategory == "Security",
            onClick = { onCategorySelected("Security") }
        )
        NotificationCategoryChip(
            text = "Promotions",
            isSelected = selectedCategory == "Promotions",
            onClick = { onCategorySelected("Promotions") }
        )
    }
}

@Composable
fun NotificationCategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = { Text(text) },
        selected = isSelected,
        modifier = Modifier.height(32.dp)
    )
}

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isUnread) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                notification.icon,
                contentDescription = null,
                tint = notification.iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (notification.isUnread) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (notification.isUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(start = 8.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {}
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = notification.timeAgo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

data class Notification(
    val title: String,
    val message: String,
    val timeAgo: String,
    val icon: ImageVector,
    val iconColor: Color,
    val isUnread: Boolean,
    val category: String
)

fun getNotifications(): List<Notification> {
    return listOf(
        Notification(
            title = "Payment Received",
            message = "You received $250.00 from John Smith for dinner last night.",
            timeAgo = "2 minutes ago",
            icon = Icons.Default.Home,
            iconColor = Color(0xFF4CAF50),
            isUnread = true,
            category = "Transactions"
        ),
        Notification(
            title = "Security Alert",
            message = "New login detected from iPhone 14 Pro. If this wasn't you, please secure your account.",
            timeAgo = "1 hour ago",
            icon = Icons.Default.Lock,
            iconColor = Color(0xFFFF9800),
            isUnread = true,
            category = "Security"
        ),
        Notification(
            title = "Card Payment Successful",
            message = "Your payment of $89.99 to Amazon was processed successfully.",
            timeAgo = "3 hours ago",
            icon = Icons.Default.Star,
            iconColor = Color(0xFF2196F3),
            isUnread = false,
            category = "Transactions"
        ),
        Notification(
            title = "Special Offer",
            message = "Get 2% cashback on all grocery purchases this month. Limited time offer!",
            timeAgo = "1 day ago",
            icon = Icons.Default.Star,
            iconColor = Color(0xFF9C27B0),
            isUnread = false,
            category = "Promotions"
        ),
        Notification(
            title = "Loan Payment Due",
            message = "Your monthly loan payment of $450.00 is due in 3 days. Set up auto-pay to avoid late fees.",
            timeAgo = "2 days ago",
            icon = Icons.Default.Home,
            iconColor = Color(0xFF607D8B),
            isUnread = false,
            category = "Transactions"
        ),
        Notification(
            title = "Crypto Price Alert",
            message = "Bitcoin has reached $45,000! Your portfolio value has increased by 12%.",
            timeAgo = "3 days ago",
            icon = Icons.Default.KeyboardArrowUp,
            iconColor = Color(0xFF4CAF50),
            isUnread = false,
            category = "Promotions"
        ),
        Notification(
            title = "Account Verification",
            message = "Your identity verification has been completed successfully.",
            timeAgo = "1 week ago",
            icon = Icons.Default.Check,
            iconColor = Color(0xFF4CAF50),
            isUnread = false,
            category = "Security"
        )
    )
}
