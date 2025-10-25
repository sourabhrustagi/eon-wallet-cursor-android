package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(
    onBackClick: () -> Unit,
    onRepaymentClick: () -> Unit = {}
) {
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isUnlocked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loan Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isUnlocked) {
            LoanDetailsContent(onRepaymentClick = onRepaymentClick)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Progress indicator - Single step
                LinearProgressIndicator(
                    progress = 1.0f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Enter OTP to unlock loan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Direct OTP verification step
                LoanOtpVerificationStep(
                    otp = otp,
                    onOtpChanged = { otp = it },
                    onBack = onBackClick,
                    onBackToHome = onBackClick,
                    onComplete = {
                        // Unlock the loan
                        coroutineScope.launch {
                            unlockPreferences.unlockLoan("loan_1")
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

@Composable
fun LoanDetailsContent(
    onRepaymentClick: () -> Unit = {}
) {
    var currentPage by remember { mutableStateOf(1) }
    val itemsPerPage = 3
    val allPayments = getLoanPaymentHistory()
    val totalPages = (allPayments.size + itemsPerPage - 1) / itemsPerPage
    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = minOf(startIndex + itemsPerPage, allPayments.size)
    val paginatedPayments = allPayments.subList(startIndex, endIndex)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Loan Preview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFE64A19)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Personal Loan",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                    Text(
                        text = "Outstanding Balance",
                        style = MaterialTheme.typography.bodyLarge,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$15,000",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
        
        // Loan Information
        item {
            Text(
                text = "Loan Information",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LoanInfoRow("Loan Type", "Personal Loan")
                    LoanInfoRow("Principal Amount", "$20,000")
                    LoanInfoRow("Interest Rate", "8.5% APR")
                    LoanInfoRow("Term", "36 months")
                    LoanInfoRow("Monthly Payment", "$650.00")
                    LoanInfoRow("Next Payment Due", "Dec 15, 2024")
                }
            }
        }
        
        // Repayment Button
        item {
            Button(
                onClick = onRepaymentClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Repayment",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Make Payment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Payment History
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment History",
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
        
        items(paginatedPayments) { payment: LoanPayment ->
            PaymentCard(payment = payment)
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
        
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun LoanInfoRow(label: String, value: String) {
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

@Composable
fun LoanOtpVerificationStep(
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
        Text(
            text = "Enter OTP",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We've sent a verification code to your registered mobile number",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = onOtpChanged,
            label = { Text("OTP") },
            placeholder = { Text("123456") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth(),
            enabled = otp.length == 6 && !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = androidx.compose.ui.graphics.Color.White
                )
            } else {
                Text("Complete")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onSendOtp,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Resend OTP")
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

data class LoanPayment(
    val date: String,
    val amount: String,
    val status: String, // "paid", "pending", "overdue"
    val paymentMethod: String
)

fun getLoanPaymentHistory(): List<LoanPayment> {
    return listOf(
        LoanPayment("Dec 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Nov 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Oct 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Sep 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Aug 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Jul 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Jun 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("May 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Apr 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Mar 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Feb 15, 2024", "$650.00", "paid", "Auto Debit"),
        LoanPayment("Jan 15, 2024", "$650.00", "paid", "Auto Debit")
    )
}

@Composable
fun PaymentCard(payment: LoanPayment) {
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
                    text = payment.date,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = payment.paymentMethod,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = payment.amount,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = payment.status.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = when (payment.status) {
                        "paid" -> Color(0xFF4CAF50)
                        "pending" -> Color(0xFFFF9800)
                        "overdue" -> Color(0xFFF44336)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
