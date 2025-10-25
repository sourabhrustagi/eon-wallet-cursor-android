package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    organizationId: String,
    onBackClick: () -> Unit,
    onDonateClick: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> }
) {
    var donorName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    
    // Validation states
    var donorNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var messageError by remember { mutableStateOf("") }
    
    val organization = getOrganizations().find { it.id == organizationId }
    
    // Validation functions
    fun validateDonorName(name: String): String {
        return when {
            name.isBlank() -> "Donor name is required"
            name.length < 2 -> "Name must be at least 2 characters"
            name.length > 50 -> "Name must be less than 50 characters"
            !name.matches(Regex("^[a-zA-Z\\s]+$")) -> "Name can only contain letters and spaces"
            else -> ""
        }
    }
    
    fun validateEmail(emailStr: String): String {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return when {
            emailStr.isBlank() -> "Email is required"
            !emailStr.matches(emailRegex) -> "Please enter a valid email address"
            emailStr.length > 100 -> "Email must be less than 100 characters"
            else -> ""
        }
    }
    
    fun validatePhone(phoneStr: String): String {
        val phoneRegex = Regex("^[+]?[0-9]{10,15}$")
        return when {
            phoneStr.isBlank() -> "Phone number is required"
            !phoneStr.matches(phoneRegex) -> "Please enter a valid phone number (10-15 digits)"
            else -> ""
        }
    }
    
    fun validateAmount(amountStr: String): String {
        return when {
            amountStr.isBlank() -> "Donation amount is required"
            amountStr.toDoubleOrNull() == null -> "Please enter a valid amount"
            amountStr.toDoubleOrNull()!! <= 0 -> "Amount must be greater than 0"
            amountStr.toDoubleOrNull()!! < 1 -> "Minimum donation is $1.00"
            amountStr.toDoubleOrNull()!! > 50000 -> "Maximum donation is $50,000"
            amountStr.contains(Regex("\\d+\\.\\d{3,}")) -> "Amount can have maximum 2 decimal places"
            else -> ""
        }
    }
    
    fun validateMessage(messageStr: String): String {
        return when {
            messageStr.length > 500 -> "Message must be less than 500 characters"
            else -> ""
        }
    }
    
    fun isFormValid(): Boolean {
        return donorNameError.isEmpty() && emailError.isEmpty() && phoneError.isEmpty() &&
               amountError.isEmpty() && messageError.isEmpty() &&
               donorName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && amount.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Make Donation") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Organization Info
            organization?.let { org ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = org.color.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(org.color, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    org.icon,
                                    contentDescription = org.name,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column {
                                Text(
                                    text = "Donating to: ${org.name}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = org.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Donor Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Donor Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = donorName,
                            onValueChange = { 
                                donorName = it
                                donorNameError = validateDonorName(it)
                            },
                            label = { Text("Full Name") },
                            placeholder = { Text("Enter your full name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            isError = donorNameError.isNotEmpty(),
                            supportingText = if (donorNameError.isNotEmpty()) {
                                { Text(donorNameError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") }
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                emailError = validateEmail(it)
                            },
                            label = { Text("Email Address") },
                            placeholder = { Text("Enter your email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = emailError.isNotEmpty(),
                            supportingText = if (emailError.isNotEmpty()) {
                                { Text(emailError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") }
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { 
                                phone = it
                                phoneError = validatePhone(it)
                            },
                            label = { Text("Phone Number") },
                            placeholder = { Text("Enter your phone number") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = phoneError.isNotEmpty(),
                            supportingText = if (phoneError.isNotEmpty()) {
                                { Text(phoneError, color = MaterialTheme.colorScheme.error) }
                            } else null,
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") }
                        )
                    }
                }
            }

            // Donation Amount
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Donation Amount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { 
                                amount = it
                                amountError = validateAmount(it)
                            },
                            label = { Text("Amount ($)") },
                            placeholder = { Text("0.00") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = amountError.isNotEmpty(),
                            supportingText = if (amountError.isNotEmpty()) {
                                { Text(amountError, color = MaterialTheme.colorScheme.error) }
                            } else {
                                { Text("Minimum: $1.00 | Maximum: $50,000") }
                            },
                            leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Amount") }
                        )
                    }
                }
            }

            // Message (Optional)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Message (Optional)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = message,
                            onValueChange = { 
                                message = it
                                messageError = validateMessage(it)
                            },
                            label = { Text("Add a message") },
                            placeholder = { Text("Why are you donating?") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 3,
                            maxLines = 5,
                            isError = messageError.isNotEmpty(),
                            supportingText = if (messageError.isNotEmpty()) {
                                { Text(messageError, color = MaterialTheme.colorScheme.error) }
                            } else {
                                { Text("${message.length}/500 characters") }
                            },
                            leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Message") }
                        )
                    }
                }
            }

            // Donate Button
            item {
                Button(
                    onClick = { showConfirmation = true },
                    enabled = isFormValid() && !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Make Donation",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    
    // Confirmation Dialog
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = {
                Text(
                    text = "Confirm Donation",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Please review your donation details:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Organization: ${organization?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Amount: $${amount}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Donor: $donorName",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Email: $email",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (message.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Message: $message",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmation = false
                        isLoading = true
                        onDonateClick(organizationId, donorName, email, phone, amount)
                    }
                ) {
                    Text("Confirm Donation")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
