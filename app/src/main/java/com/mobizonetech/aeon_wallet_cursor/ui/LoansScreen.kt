package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoansScreen(
    onLoanClick: () -> Unit,
    onBankProductsClick: () -> Unit = {},
    onLoanApplyClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val unlockedLoans by unlockPreferences.unlockedLoans.collectAsState(initial = emptySet())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Loans",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Combined Loans Section
            item {
                val allLoans = getActiveLoans()
                val unlockedLoansList = allLoans.filter { unlockedLoans.contains(it.id) }
                val lockedLoans = allLoans.filter { !unlockedLoans.contains(it.id) }
                val combinedLoans = unlockedLoansList + lockedLoans
                
                if (combinedLoans.isNotEmpty()) {
                    Text(
                        text = "My Loans",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            
            item {
                val allLoans = getActiveLoans()
                val unlockedLoansList = allLoans.filter { unlockedLoans.contains(it.id) }
                val lockedLoans = allLoans.filter { !unlockedLoans.contains(it.id) }
                val combinedLoans = unlockedLoansList + lockedLoans
                
                if (combinedLoans.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        combinedLoans.forEach { loan ->
                            if (unlockedLoans.contains(loan.id)) {
                                UnlockedLoanCard(
                                    loan = loan,
                                    onClick = onLoanClick
                                )
                            } else {
                                LoanCard(
                                    loan = loan,
                                    onClick = onLoanClick
                                )
                            }
                        }
                    }
                }
            }
            
            // Bank Products Section (Loans)
            item {
                BankProductsLoansSection(
                    onBankProductsClick = onBankProductsClick,
                    onLoanApplyClick = onLoanApplyClick
                )
            }
            
        }
    }
}


@Composable
fun BankProductsLoansSection(
    onBankProductsClick: () -> Unit,
    onLoanApplyClick: (String) -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Apply",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onBankProductsClick) {
                Text("View All")
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            // Personal Loan
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                onClick = { onLoanApplyClick("personal_loan") },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF1976D2)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Personal Loan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Up to $50,000",
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "6.99% APR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
            
            // Home Loan
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                onClick = { onLoanApplyClick("home_loan") },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF388E3C)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Home Loan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Up to $2M",
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "4.25% APR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
            
            // Auto Loan
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                onClick = { onLoanApplyClick("auto_loan") },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFD32F2F)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Auto Loan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Up to $100K",
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "3.99% APR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
            
            // Business Loan
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                onClick = { onLoanApplyClick("business_loan") },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF795548)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Business Loan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Up to $500K",
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "5.50% APR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LoanCard(
    loan: LoanItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (loan.isLocked) 
                loan.backgroundColor.copy(alpha = 0.4f) 
            else loan.backgroundColor
        )
    ) {
        // Lock overlay for locked loans
        if (loan.isLocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "LOCKED",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = loan.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = loan.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = loan.textColor
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        loan.icon,
                        contentDescription = null,
                        tint = loan.textColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Column {
                Text(
                    text = loan.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = loan.textColor
                )
                Text(
                    text = loan.amount,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = loan.textColor
                )
            }
        }
    }
}


@Composable
fun LoanHistoryItem(loan: LoanHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = loan.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = loan.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = loan.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = loan.amount,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (loan.isPaid) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
                )
                Text(
                    text = if (loan.isPaid) "Paid" else "Outstanding",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (loan.isPaid) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


data class LoanItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color,
    val isLocked: Boolean = false
)

data class LoanHistoryItem(
    val title: String,
    val date: String,
    val amount: String,
    val icon: ImageVector,
    val isPaid: Boolean
)

fun getActiveLoans(): List<LoanItem> = listOf(
    LoanItem(
        id = "loan_1",
        title = "Personal Loan",
        subtitle = "Outstanding",
        amount = "$15,000",
        icon = Icons.Default.Home,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFFE64A19),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = true
    ),
    LoanItem(
        id = "loan_2",
        title = "Home Loan",
        subtitle = "Outstanding",
        amount = "$250,000",
        icon = Icons.Default.Home,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF7B1FA2),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = false
    ),
    LoanItem(
        id = "loan_3",
        title = "Car Loan",
        subtitle = "Outstanding",
        amount = "$35,000",
        icon = Icons.Default.Home,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF1976D2),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = true
    )
)

fun getLoanHistory(): List<LoanHistoryItem> = listOf(
    LoanHistoryItem(
        title = "Education Loan",
        date = "Completed - Jan 2023",
        amount = "$25,000",
        icon = Icons.Default.Home,
        isPaid = true
    ),
    LoanHistoryItem(
        title = "Business Loan",
        date = "Completed - Dec 2022",
        amount = "$50,000",
        icon = Icons.Default.Home,
        isPaid = true
    ),
    LoanHistoryItem(
        title = "Personal Loan",
        date = "Completed - Nov 2022",
        amount = "$10,000",
        icon = Icons.Default.Home,
        isPaid = true
    )
)

@Composable
fun UnlockedLoanCard(
    loan: LoanItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = loan.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = loan.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = loan.textColor
                )
                Icon(
                    loan.icon,
                    contentDescription = null,
                    tint = loan.textColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    text = loan.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = loan.textColor
                )
                Text(
                    text = loan.amount,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = loan.textColor
                )
            }
        }
    }
}

