package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankProductsScreen(
    onBackClick: () -> Unit,
    onApplyForCard: (BankCard) -> Unit,
    onApplyForLoan: (BankLoan) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bank Products") },
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            // Credit Cards Section
            item {
                Text(
                    text = "Credit Cards",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(getBankCards()) { card ->
                BankCardItem(
                    card = card,
                    onApplyClick = { onApplyForCard(card) }
                )
            }
            
            // Personal Loans Section
            item {
                Text(
                    text = "Personal Loans",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            
            items(getBankLoans()) { loan ->
                BankLoanItem(
                    loan = loan,
                    onApplyClick = { onApplyForLoan(loan) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun BankCardItem(
    card: BankCard,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = card.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = card.textColor
                    )
                    Text(
                        text = card.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = card.textColor.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = card.icon,
                    contentDescription = null,
                    tint = card.textColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Features
            card.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = card.textColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall,
                        color = card.textColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Credit Limit",
                        style = MaterialTheme.typography.bodySmall,
                        color = card.textColor.copy(alpha = 0.7f)
                    )
                    Text(
                        text = card.creditLimit,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = card.textColor
                    )
                }
                
                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = card.textColor,
                        contentColor = card.backgroundColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Now")
                }
            }
        }
    }
}

@Composable
fun BankLoanItem(
    loan: BankLoan,
    onApplyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = loan.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = loan.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = loan.textColor
                    )
                    Text(
                        text = loan.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = loan.textColor.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = loan.icon,
                    contentDescription = null,
                    tint = loan.textColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Features
            loan.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = loan.textColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall,
                        color = loan.textColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Interest Rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = loan.textColor.copy(alpha = 0.7f)
                    )
                    Text(
                        text = loan.interestRate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = loan.textColor
                    )
                }
                
                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = loan.textColor,
                        contentColor = loan.backgroundColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Now")
                }
            }
        }
    }
}

// Data Classes
data class BankCard(
    val id: String,
    val name: String,
    val subtitle: String,
    val creditLimit: String,
    val features: List<String>,
    val icon: ImageVector,
    val backgroundColor: Color,
    val textColor: Color
)

data class BankLoan(
    val id: String,
    val name: String,
    val subtitle: String,
    val interestRate: String,
    val features: List<String>,
    val icon: ImageVector,
    val backgroundColor: Color,
    val textColor: Color
)

// Sample Data
fun getBankCards(): List<BankCard> = listOf(
    BankCard(
        id = "premium_card",
        name = "Aeon Premium Card",
        subtitle = "Exclusive benefits for premium customers",
        creditLimit = "Up to $50,000",
        features = listOf(
            "5% cashback on all purchases",
            "Complimentary airport lounge access",
            "Travel insurance coverage",
            "24/7 concierge service",
            "No foreign transaction fees"
        ),
        icon = Icons.Default.Star,
        backgroundColor = Color(0xFF1A237E),
        textColor = Color.White
    ),
    BankCard(
        id = "cashback_card",
        name = "Aeon Cashback Card",
        subtitle = "Earn cashback on every purchase",
        creditLimit = "Up to $25,000",
        features = listOf(
            "3% cashback on groceries",
            "2% cashback on gas",
            "1% cashback on all other purchases",
            "No annual fee",
            "Mobile app management"
        ),
        icon = Icons.Default.Star,
        backgroundColor = Color(0xFF2E7D32),
        textColor = Color.White
    ),
    BankCard(
        id = "travel_card",
        name = "Aeon Travel Card",
        subtitle = "Perfect for frequent travelers",
        creditLimit = "Up to $35,000",
        features = listOf(
            "3x points on travel bookings",
            "2x points on dining",
            "Free checked bags",
            "Travel accident insurance",
            "Emergency card replacement"
        ),
        icon = Icons.Default.Star,
        backgroundColor = Color(0xFFE65100),
        textColor = Color.White
    ),
    BankCard(
        id = "student_card",
        name = "Aeon Student Card",
        subtitle = "Designed for students",
        creditLimit = "Up to $5,000",
        features = listOf(
            "No annual fee",
            "Low interest rate",
            "Credit building tools",
            "Mobile banking app",
            "Student discounts"
        ),
        icon = Icons.Default.Person,
        backgroundColor = Color(0xFF7B1FA2),
        textColor = Color.White
    )
)

fun getBankLoans(): List<BankLoan> = listOf(
    BankLoan(
        id = "personal_loan",
        name = "Personal Loan",
        subtitle = "Flexible financing for your needs",
        interestRate = "6.99% - 15.99% APR",
        features = listOf(
            "Loan amounts from $1,000 to $50,000",
            "Flexible repayment terms",
            "Quick approval process",
            "No prepayment penalties",
            "Fixed monthly payments"
        ),
        icon = Icons.Default.Home,
        backgroundColor = Color(0xFF1976D2),
        textColor = Color.White
    ),
    BankLoan(
        id = "home_loan",
        name = "Home Loan",
        subtitle = "Make your dream home a reality",
        interestRate = "4.25% - 7.50% APR",
        features = listOf(
            "Loan amounts up to $2,000,000",
            "30-year repayment terms",
            "Low down payment options",
            "Pre-approval available",
            "Dedicated loan officer"
        ),
        icon = Icons.Default.Home,
        backgroundColor = Color(0xFF388E3C),
        textColor = Color.White
    ),
    BankLoan(
        id = "auto_loan",
        name = "Auto Loan",
        subtitle = "Drive away with your new car",
        interestRate = "3.99% - 8.99% APR",
        features = listOf(
            "Loan amounts up to $100,000",
            "Up to 84-month terms",
            "Competitive rates",
            "Online application",
            "Quick funding"
        ),
        icon = Icons.Default.Home,
        backgroundColor = Color(0xFFD32F2F),
        textColor = Color.White
    ),
    BankLoan(
        id = "business_loan",
        name = "Business Loan",
        subtitle = "Fuel your business growth",
        interestRate = "5.50% - 12.99% APR",
        features = listOf(
            "Loan amounts up to $500,000",
            "Flexible repayment options",
            "Business credit building",
            "Dedicated business banker",
            "Working capital solutions"
        ),
        icon = Icons.Default.Home,
        backgroundColor = Color(0xFF795548),
        textColor = Color.White
    ),
    BankLoan(
        id = "education_loan",
        name = "Education Loan",
        subtitle = "Invest in your future",
        interestRate = "4.99% - 9.99% APR",
        features = listOf(
            "Loan amounts up to $200,000",
            "Deferred payment options",
            "No origination fees",
            "Cosigner release option",
            "Career services included"
        ),
        icon = Icons.Default.Person,
        backgroundColor = Color(0xFF9C27B0),
        textColor = Color.White
    )
)
