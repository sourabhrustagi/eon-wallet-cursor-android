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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.CardRepaymentViewModel
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRepaymentScreen(
    cardId: String,
    onBackClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val viewModelFactory = remember { ViewModelFactory(unlockPreferences) }
    val viewModel: CardRepaymentViewModel = viewModel(factory = viewModelFactory)
    
    val cardData by viewModel.cardData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var selectedPaymentType by remember { mutableStateOf("minimum") }
    var customAmount by remember { mutableStateOf("") }
    var customAmountError by remember { mutableStateOf("") }
    var showSuccessScreen by remember { mutableStateOf(false) }
    
    // Load card data when screen is created
    LaunchedEffect(cardId) {
        viewModel.loadCardData(cardId)
    }
    
    // Create local variable for cardData to avoid nullable issues
    val currentCardData = cardData
    
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
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (error != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error!!,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            } else if (currentCardData != null) {
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
                                text = currentCardData.cardName,
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
                                    text = currentCardData.currentBalance,
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
                                    text = currentCardData.dueDate,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
            
            // Payment Options - Only show when cardData is available
            if (currentCardData != null) {
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
                        amount = currentCardData.minimumPayment,
                        description = "Avoid late fees",
                        isSelected = selectedPaymentType == "minimum",
                        onClick = { selectedPaymentType = "minimum" }
                    )
                }
                
                // Full Payment
                item {
                    PaymentOptionCard(
                        title = "Full Balance",
                        amount = currentCardData.fullBalance,
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
            }
            
            // Custom Amount Input
            if (selectedPaymentType == "partial") {
                item {
                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = {
                            customAmount = it
                            customAmountError = viewModel.validateCustomAmount(it)
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
            
            // Payment Summary - Only show when cardData is available
            if (currentCardData != null) {
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
                                        "minimum" -> currentCardData.minimumPayment
                                        "full" -> currentCardData.fullBalance
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
                                    text = viewModel.calculateTotalAmount(selectedPaymentType, customAmount),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            
            // Pay Now Button - Only show when cardData is available
            if (currentCardData != null) {
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
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
    
    // Payment Success Screen
    if (showSuccessScreen && currentCardData != null) {
        PaymentSuccessScreen(
            paymentType = "Card Repayment",
            amount = viewModel.calculateTotalAmount(selectedPaymentType, customAmount),
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
