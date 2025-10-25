package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var subjectError by remember { mutableStateOf("") }
    var messageError by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contact Us",
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
            
            // Contact Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Get in Touch",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        ContactInfoRow(
                            icon = Icons.Default.Star,
                            label = "Phone",
                            value = "+1 (555) 123-4567"
                        )
                        
                        ContactInfoRow(
                            icon = Icons.Default.Star,
                            label = "Email",
                            value = "support@aeonbank.com"
                        )
                        
                        ContactInfoRow(
                            icon = Icons.Default.Star,
                            label = "Address",
                            value = "123 Financial Street, City, State 12345"
                        )
                        
                        ContactInfoRow(
                            icon = Icons.Default.Star,
                            label = "Hours",
                            value = "Mon-Fri: 9AM-6PM EST"
                        )
                    }
                }
            }
            
            // Contact Form
            item {
                Text(
                    text = "Send us a Message",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Name Field
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = if (it.isEmpty()) "Name is required" else ""
                    },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = nameError.isNotEmpty(),
                    supportingText = if (nameError.isNotEmpty()) {
                        { Text(nameError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") }
                )
            }
            
            // Email Field
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (it.isEmpty()) "Email is required" 
                        else if (!it.contains("@")) "Please enter a valid email" 
                        else ""
                    },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = emailError.isNotEmpty(),
                    supportingText = if (emailError.isNotEmpty()) {
                        { Text(emailError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Email") }
                )
            }
            
            // Phone Field
            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = if (it.isEmpty()) "Phone number is required" else ""
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneError.isNotEmpty(),
                    supportingText = if (phoneError.isNotEmpty()) {
                        { Text(phoneError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Phone") }
                )
            }
            
            // Subject Field
            item {
                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                        subjectError = if (it.isEmpty()) "Subject is required" else ""
                    },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = subjectError.isNotEmpty(),
                    supportingText = if (subjectError.isNotEmpty()) {
                        { Text(subjectError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Subject") }
                )
            }
            
            // Message Field
            item {
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it
                        messageError = if (it.isEmpty()) "Message is required" 
                        else if (it.length < 10) "Message must be at least 10 characters" 
                        else ""
                    },
                    label = { Text("Message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    isError = messageError.isNotEmpty(),
                    supportingText = if (messageError.isNotEmpty()) {
                        { Text(messageError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Message") }
                )
            }
            
            // Submit Button
            item {
                Button(
                    onClick = {
                        // Handle form submission
                    },
                    enabled = name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && 
                             subject.isNotEmpty() && message.isNotEmpty() && 
                             nameError.isEmpty() && emailError.isEmpty() && phoneError.isEmpty() && 
                             subjectError.isEmpty() && messageError.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = "Send Message",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ContactInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
