package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun UtilityPaymentScreen(
    billId: String,
    onBackClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    var accountNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    
    var accountNumberError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var customerNameError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var showSuccessScreen by remember { mutableStateOf(false) }
    
    // Get bill information based on billId
    val billInfo = remember(billId) {
        getUtilityBillInfo(billId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pay ${billInfo.name}",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Bill Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = billInfo.backgroundColor.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        billInfo.icon,
                        contentDescription = null,
                        tint = billInfo.backgroundColor,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = billInfo.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = billInfo.backgroundColor
                        )
                        Text(
                            text = billInfo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Payment Form
            Text(
                text = "Payment Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Account Number Field
            OutlinedTextField(
                value = accountNumber,
                onValueChange = {
                    accountNumber = it
                    accountNumberError = if (it.isEmpty()) "Account number is required" 
                    else if (it.length < 8) "Account number must be at least 8 digits" 
                    else ""
                },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = accountNumberError.isNotEmpty(),
                supportingText = if (accountNumberError.isNotEmpty()) {
                    { Text(accountNumberError, color = MaterialTheme.colorScheme.error) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Account") }
            )
            
            // Customer Name Field
            OutlinedTextField(
                value = customerName,
                onValueChange = {
                    customerName = it
                    customerNameError = if (it.isEmpty()) "Customer name is required" 
                    else if (it.length < 2) "Name must be at least 2 characters" 
                    else ""
                },
                label = { Text("Customer Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = customerNameError.isNotEmpty(),
                supportingText = if (customerNameError.isNotEmpty()) {
                    { Text(customerNameError, color = MaterialTheme.colorScheme.error) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") }
            )
            
            // Phone Number Field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneNumberError = if (it.isEmpty()) "Phone number is required" 
                    else if (it.length < 10) "Phone number must be at least 10 digits" 
                    else ""
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneNumberError.isNotEmpty(),
                supportingText = if (phoneNumberError.isNotEmpty()) {
                    { Text(phoneNumberError, color = MaterialTheme.colorScheme.error) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Phone") }
            )
            
            // Email Field
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError.isNotEmpty(),
                supportingText = if (emailError.isNotEmpty()) {
                    { Text(emailError, color = MaterialTheme.colorScheme.error) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Email") }
            )
            
            // Amount Field
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = if (it.isEmpty()) "Amount is required" 
                    else if (it.toDoubleOrNull() == null) "Please enter a valid amount" 
                    else if (it.toDoubleOrNull()!! < 1.0) "Amount must be at least $1.00" 
                    else ""
                },
                label = { Text("Amount") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = amountError.isNotEmpty(),
                supportingText = if (amountError.isNotEmpty()) {
                    { Text(amountError, color = MaterialTheme.colorScheme.error) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Amount") }
            )
            
            // Payment Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Payment Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Bill Amount",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = if (amount.isNotEmpty()) "$$amount" else "$0.00",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Processing Fee",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "$2.50",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    HorizontalDivider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (amount.isNotEmpty()) "$${String.format("%.2f", amount.toDoubleOrNull()?.plus(2.50) ?: 0.0)}" else "$2.50",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Pay Now Button
            Button(
                onClick = { showSuccessScreen = true },
                enabled = accountNumber.isNotEmpty() && customerName.isNotEmpty() && 
                         phoneNumber.isNotEmpty() && email.isNotEmpty() && amount.isNotEmpty() &&
                         accountNumberError.isEmpty() && customerNameError.isEmpty() && 
                         phoneNumberError.isEmpty() && emailError.isEmpty() && amountError.isEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Payment",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pay Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    
    // Payment Success Screen
    if (showSuccessScreen) {
        PaymentSuccessScreen(
            paymentType = "${billInfo.name} Bill Payment",
            amount = if (amount.isNotEmpty()) "$${String.format("%.2f", amount.toDoubleOrNull()?.plus(2.50) ?: 0.0)}" else "$2.50",
            transactionId = "TXN${System.currentTimeMillis()}",
            onBackToHome = {
                showSuccessScreen = false
                onPaymentComplete()
            },
            onViewReceipt = {
                // Handle view receipt - could navigate to receipt screen
                showSuccessScreen = false
                onPaymentComplete()
            }
        )
    }
}

data class UtilityBillInfo(
    val id: String,
    val name: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

fun getUtilityBillInfo(billId: String): UtilityBillInfo {
    return when (billId) {
        "electricity" -> UtilityBillInfo(
            id = "electricity",
            name = "Electricity",
            description = "Pay your electricity bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
        )
        "mobile" -> UtilityBillInfo(
            id = "mobile",
            name = "Mobile Recharge",
            description = "Recharge your mobile phone",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
        )
        "water" -> UtilityBillInfo(
            id = "water",
            name = "Water Bill",
            description = "Pay your water bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF00BCD4)
        )
        "gas" -> UtilityBillInfo(
            id = "gas",
            name = "Gas Bill",
            description = "Pay your gas bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFFF9800)
        )
        "internet" -> UtilityBillInfo(
            id = "internet",
            name = "Internet",
            description = "Pay your internet bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
        )
        "cable" -> UtilityBillInfo(
            id = "cable",
            name = "Cable TV",
            description = "Pay your cable TV bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFFE91E63)
        )
        else -> UtilityBillInfo(
            id = "unknown",
            name = "Unknown Bill",
            description = "Pay your bill",
            icon = Icons.Default.Star,
            backgroundColor = androidx.compose.ui.graphics.Color(0xFF757575)
        )
    }
}
