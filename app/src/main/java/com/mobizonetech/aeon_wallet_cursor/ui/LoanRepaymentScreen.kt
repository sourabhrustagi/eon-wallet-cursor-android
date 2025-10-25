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
fun LoanRepaymentScreen(
    loanId: String,
    onBackClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    var selectedPaymentType by remember { mutableStateOf("minimum") }
    var customAmount by remember { mutableStateOf("") }
    var customAmountError by remember { mutableStateOf("") }
    var showSuccessScreen by remember { mutableStateOf(false) }
    
    // Sample loan data
    val loanData = remember {
        LoanRepaymentData(
            loanId = loanId,
            loanName = "Personal Loan",
            currentBalance = "$15,000.00",
            minimumPayment = "$450.00",
            fullBalance = "$15,000.00",
            dueDate = "Jan 15, 2025",
            interestRate = "8.5%"
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Loan Repayment",
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
            
            // Loan Information
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
                            text = loanData.loanName,
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
                                text = loanData.currentBalance,
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
                                text = "Interest Rate",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = loanData.interestRate,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
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
                                text = loanData.dueDate,
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
                    amount = loanData.minimumPayment,
                    description = "Avoid late fees",
                    isSelected = selectedPaymentType == "minimum",
                    onClick = { selectedPaymentType = "minimum" }
                )
            }
            
            // Full Payment
            item {
                PaymentOptionCard(
                    title = "Full Balance",
                    amount = loanData.fullBalance,
                    description = "Pay off entire loan",
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
                            } else if (it.isNotEmpty() && it.toDoubleOrNull() != null && it.toDouble() < 450.0) {
                                "Amount must be at least $450.00"
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
                                    "minimum" -> loanData.minimumPayment
                                    "full" -> loanData.fullBalance
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
                                text = "$5.00",
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
                                    "minimum" -> "$455.00"
                                    "full" -> "$15,005.00"
                                    "partial" -> if (customAmount.isNotEmpty()) "$${String.format("%.2f", customAmount.toDoubleOrNull()?.plus(5.00) ?: 0.0)}" else "$5.00"
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
                    onClick = { showSuccessScreen = true },
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
    
    // Payment Success Screen
    if (showSuccessScreen) {
        PaymentSuccessScreen(
            paymentType = "Loan Repayment",
            amount = when (selectedPaymentType) {
                "minimum" -> loanData.minimumPayment
                "full" -> loanData.fullBalance
                "partial" -> if (customAmount.isNotEmpty()) "$$customAmount" else "$0.00"
                else -> "$0.00"
            },
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

data class LoanRepaymentData(
    val loanId: String,
    val loanName: String,
    val currentBalance: String,
    val minimumPayment: String,
    val fullBalance: String,
    val dueDate: String,
    val interestRate: String
)
