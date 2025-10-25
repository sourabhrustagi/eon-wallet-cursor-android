package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String = "card_1",
    onBackClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    var cvv by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isUnlocked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val unlockedCards by unlockPreferences.unlockedCards.collectAsState(initial = emptySet())
    
    // Check if card is already unlocked
    LaunchedEffect(cardId) {
        isUnlocked = unlockedCards.contains(cardId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isUnlocked) {
            // Show card details after successful unlock
            CardDetailsContent()
        } else {
            // Show unlock flow
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Progress indicator
                LinearProgressIndicator(
                    progress = currentStep.toFloat() / 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Step $currentStep of 2",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                when (currentStep) {
                    1 -> CvvVerificationStep(
                        cvv = cvv,
                        onCvvChanged = { cvv = it },
                        onNext = { if (cvv.length == 3) currentStep = 2 },
                        isLoading = isLoading,
                        onVerifyCvv = {
                            isLoading = true
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(2000)
                                isLoading = false
                            }
                        }
                    )
                    2 -> OtpVerificationStep(
                        otp = otp,
                        onOtpChanged = { otp = it },
                        onBack = { currentStep = 1 },
                        onBackToHome = onBackClick,
                    onComplete = {
                        // Unlock the card
                        coroutineScope.launch {
                            unlockPreferences.unlockCard(cardId)
                            isUnlocked = true
                        }
                    },
                        isLoading = isLoading,
                        onSendOtp = {
                            isLoading = true
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(2000)
                                isLoading = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CvvVerificationStep(
    cvv: String,
    onCvvChanged: (String) -> Unit,
    onNext: () -> Unit,
    isLoading: Boolean,
    onVerifyCvv: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card Preview
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(180.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF1976D2)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Aeon Bank",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "**** **** **** 1234",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "John Doe",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "CVV: ***",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "12/25",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Enter CVV to Continue",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Please enter the 3-digit CVV code from the back of your card",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = cvv,
            onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) onCvvChanged(it) },
            label = { Text("CVV") },
            placeholder = { Text("123") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { Text("Enter 3 digits") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onVerifyCvv()
                onNext()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = cvv.length == 3 && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Verify CVV")
            }
        }
    }
}

@Composable
fun OtpVerificationStep(
    otp: String,
    onOtpChanged: (String) -> Unit,
    onBack: () -> Unit,
    onBackToHome: () -> Unit,
    onComplete: () -> Unit,
    isLoading: Boolean,
    onSendOtp: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify with OTP",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We've sent a verification code to your registered mobile number",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) onOtpChanged(it) },
            label = { Text("Enter OTP") },
            placeholder = { Text("123456") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { Text("Enter the 6-digit OTP") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSendOtp,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Resend OTP")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onComplete,
                modifier = Modifier.weight(1f),
                enabled = otp.length == 6
            ) {
                Text("Complete")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
fun CardDetailsContent() {
    var currentPage by remember { mutableStateOf(1) }
    val itemsPerPage = 3
    val allTransactions = getRecentCardTransactions()
    val totalPages = (allTransactions.size + itemsPerPage - 1) / itemsPerPage
    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = minOf(startIndex + itemsPerPage, allTransactions.size)
    val paginatedTransactions = allTransactions.subList(startIndex, endIndex)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Card Preview
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1976D2)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Aeon Bank",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "4532 1234 5678 9012",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "John Doe",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CVV: 123",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "12/25",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        
        // Card Information
        item {
            Text(
                text = "Card Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CardInfoRow("Card Number", "4532 1234 5678 9012")
                    CardInfoRow("Cardholder Name", "John Doe")
                    CardInfoRow("Expiry Date", "12/25")
                    CardInfoRow("CVV", "123")
                    CardInfoRow("Card Type", "Visa Platinum")
                    CardInfoRow("Issuing Bank", "Aeon Bank")
                }
            }
        }
        
        // Account Details
        item {
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CardInfoRow("Available Balance", "$12,450.75")
                    CardInfoRow("Credit Limit", "$25,000.00")
                    CardInfoRow("Used Credit", "$12,549.25")
                    CardInfoRow("Minimum Payment", "$125.00")
                    CardInfoRow("Due Date", "Dec 15, 2024")
                    CardInfoRow("Interest Rate", "18.99% APR")
                }
            }
        }
        
        // Recent Transactions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Page $currentPage of $totalPages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        items(paginatedTransactions) { transaction: CardTransaction ->
            TransactionCard(transaction = transaction)
        }
        
        // Pagination Controls
        if (totalPages > 1) {
            item {
                PaginationControls(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = { page -> currentPage = page }
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CardInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

data class CardTransaction(
    val description: String,
    val amount: String,
    val date: String,
    val type: String // "debit" or "credit"
)

fun getRecentCardTransactions(): List<CardTransaction> {
    return listOf(
        CardTransaction("Amazon Purchase", "-$89.99", "Dec 10, 2024", "debit"),
        CardTransaction("Gas Station", "-$45.50", "Dec 9, 2024", "debit"),
        CardTransaction("Payment Received", "+$250.00", "Dec 8, 2024", "credit"),
        CardTransaction("Grocery Store", "-$67.25", "Dec 7, 2024", "debit"),
        CardTransaction("Restaurant", "-$32.80", "Dec 6, 2024", "debit"),
        CardTransaction("Netflix Subscription", "-$15.99", "Dec 5, 2024", "debit"),
        CardTransaction("Coffee Shop", "-$8.50", "Dec 4, 2024", "debit"),
        CardTransaction("Online Shopping", "-$156.75", "Dec 3, 2024", "debit"),
        CardTransaction("ATM Withdrawal", "-$100.00", "Dec 2, 2024", "debit"),
        CardTransaction("Salary Credit", "+$3,500.00", "Dec 1, 2024", "credit"),
        CardTransaction("Utility Bill", "-$85.30", "Nov 30, 2024", "debit"),
        CardTransaction("Insurance Payment", "-$120.00", "Nov 29, 2024", "debit")
    )
}

@Composable
fun TransactionCard(transaction: CardTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == "credit") Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}

@Composable
fun PaginationControls(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            OutlinedButton(
                onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
                enabled = currentPage > 1,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Previous")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Page Numbers
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val startPage = maxOf(1, currentPage - 1)
                val endPage = minOf(totalPages, currentPage + 1)
                
                for (page in startPage..endPage) {
                    FilterChip(
                        onClick = { onPageChange(page) },
                        label = { Text(page.toString()) },
                        selected = page == currentPage,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Next Button
            OutlinedButton(
                onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) },
                enabled = currentPage < totalPages,
                modifier = Modifier.weight(1f)
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
    }
}
