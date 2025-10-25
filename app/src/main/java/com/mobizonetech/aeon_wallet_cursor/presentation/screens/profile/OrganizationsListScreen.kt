package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizationsListScreen(
    onBackClick: () -> Unit,
    onOrganizationClick: (String) -> Unit = {}
) {
    val organizations = getOrganizations()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Organization") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Text(
                    text = "Support a Cause",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            item {
                Text(
                    text = "Select an organization to make a donation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            items(organizations) { organization ->
                OrganizationCard(
                    organization = organization,
                    onClick = { onOrganizationClick(organization.id) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun OrganizationCard(
    organization: Organization,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Organization Logo/Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(organization.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    organization.icon,
                    contentDescription = organization.name,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Organization Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = organization.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = organization.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = organization.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Arrow Icon
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "View Details",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class Organization(
    val id: String,
    val name: String,
    val description: String,
    val location: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

fun getOrganizations(): List<Organization> {
    return listOf(
        Organization(
            id = "red_cross",
            name = "Red Cross",
            description = "Emergency relief and disaster response worldwide",
            location = "Global",
            icon = Icons.Default.Favorite,
            color = Color(0xFFD32F2F)
        ),
        Organization(
            id = "unicef",
            name = "UNICEF",
            description = "Children's rights and humanitarian aid",
            location = "Global",
            icon = Icons.Default.Person,
            color = Color(0xFF1976D2)
        ),
        Organization(
            id = "doctors_without_borders",
            name = "Doctors Without Borders",
            description = "Medical humanitarian assistance",
            location = "Global",
            icon = Icons.Default.Home,
            color = Color(0xFF388E3C)
        ),
        Organization(
            id = "world_wildlife_fund",
            name = "World Wildlife Fund",
            description = "Conservation of nature and wildlife",
            location = "Global",
            icon = Icons.Default.Star,
            color = Color(0xFF4CAF50)
        ),
        Organization(
            id = "oxfam",
            name = "Oxfam",
            description = "Fighting global poverty and inequality",
            location = "Global",
            icon = Icons.Default.Home,
            color = Color(0xFFFF9800)
        ),
        Organization(
            id = "amnesty_international",
            name = "Amnesty International",
            description = "Human rights advocacy and protection",
            location = "Global",
            icon = Icons.Default.Home,
            color = Color(0xFF9C27B0)
        ),
        Organization(
            id = "local_food_bank",
            name = "Local Food Bank",
            description = "Fighting hunger in our community",
            location = "Local",
            icon = Icons.Default.Home,
            color = Color(0xFF795548)
        ),
        Organization(
            id = "education_foundation",
            name = "Education Foundation",
            description = "Supporting quality education for all",
            location = "Regional",
            icon = Icons.Default.Home,
            color = Color(0xFF607D8B)
        )
    )
}
