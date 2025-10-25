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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRepaymentScreen(
    cardId: String,
    onBackClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    var selectedPaymentType by remember { mutableStateOf("minimum") }
    var customAmount by remember { mutableStateOf("") }
    var customAmountError by remember { mutableStateOf("") }
    
    // Sample card data
    val cardData = remember {
        CardRepaymentData(
            cardId = cardId,
            cardName = "Credit Card",
            currentBalance = "$2,500.00",
            minimumPayment = "$75.00",
            fullBalance = "$2,500.00",
            dueDate = "Dec 25, 2024"
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Card Repayment",
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
            
            // Card Information
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
                            text = cardData.cardName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Current Balance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = cardData.currentBalance,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Due Date",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = cardData.dueDate,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            
            // Payment Options
            item {
                Text(
                    text = "Payment Options",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Minimum Payment
            item {
                PaymentOptionCard(
                    title = "Minimum Payment",
                    amount = cardData.minimumPayment,
                    description = "Avoid late fees",
                    isSelected = selectedPaymentType == "minimum",
                    onClick = { selectedPaymentType = "minimum" }
                )
            }
            
            // Full Payment
            item {
                PaymentOptionCard(
                    title = "Full Balance",
                    amount = cardData.fullBalance,
                    description = "Pay off entire balance",
                    isSelected = selectedPaymentType == "full",
                    onClick = { selectedPaymentType = "full" }
                )
            }
            
            // Partial Payment
            item {
                PaymentOptionCard(
                    title = "Custom Amount",
                    amount = if (customAmount.isNotEmpty()) "$$customAmount" else "Enter amount",
                    description = "Pay any amount above minimum",
                    isSelected = selectedPaymentType == "partial",
                    onClick = { selectedPaymentType = "partial" }
                )
            }
            
            // Custom Amount Input
            if (selectedPaymentType == "partial") {
                item {
                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = {
                            customAmount = it
                            customAmountError = if (it.isNotEmpty() && it.toDoubleOrNull() == null) {
                                "Please enter a valid amount"
                            } else if (it.isNotEmpty() && it.toDoubleOrNull() != null && it.toDouble() < 75.0) {
                                "Amount must be at least $75.00"
                            } else {
                                ""
                            }
                        },
                        label = { Text("Enter amount") },
                        placeholder = { Text("0.00") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = customAmountError.isNotEmpty(),
                        supportingText = if (customAmountError.isNotEmpty()) {
                            { Text(customAmountError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Amount") }
                    )
                }
            }
            
            // Payment Summary
            item {
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
                                text = "Payment Amount",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = when (selectedPaymentType) {
                                    "minimum" -> cardData.minimumPayment
                                    "full" -> cardData.fullBalance
                                    "partial" -> if (customAmount.isNotEmpty()) "$$customAmount" else "$0.00"
                                    else -> "$0.00"
                                },
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
                        
                        Divider()
                        
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
                                text = when (selectedPaymentType) {
                                    "minimum" -> "$77.50"
                                    "full" -> "$2,502.50"
                                    "partial" -> if (customAmount.isNotEmpty()) "$${String.format("%.2f", customAmount.toDoubleOrNull()?.plus(2.50) ?: 0.0)}" else "$2.50"
                                    else -> "$0.00"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Pay Now Button
            item {
                Button(
                    onClick = onPaymentComplete,
                    enabled = selectedPaymentType != "partial" || (customAmount.isNotEmpty() && customAmountError.isEmpty()),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = "Pay Now",
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
fun PaymentOptionCard(
    title: String,
    amount: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else MaterialTheme.colorScheme.surfaceVariant
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class CardRepaymentData(
    val cardId: String,
    val cardName: String,
    val currentBalance: String,
    val minimumPayment: String,
    val fullBalance: String,
    val dueDate: String
)
