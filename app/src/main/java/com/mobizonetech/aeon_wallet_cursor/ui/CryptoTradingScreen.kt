package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.background
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
fun CryptoTradingScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("buy") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crypto Trading",
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
        ) {
            // Tab Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    onClick = { selectedTab = "buy" },
                    label = { Text("Buy") },
                    selected = selectedTab == "buy",
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    onClick = { selectedTab = "sell" },
                    label = { Text("Sell") },
                    selected = selectedTab == "sell",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            when (selectedTab) {
                "buy" -> BuyCryptoScreen()
                "sell" -> SellCryptoScreen()
            }
        }
    }
}

@Composable
fun BuyCryptoScreen() {
    var selectedCrypto by remember { mutableStateOf("BTC") }
    var amount by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Crypto Selection
        Text(
            text = "Select Cryptocurrency",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getCryptoList()) { crypto ->
                CryptoSelectionCard(
                    crypto = crypto,
                    isSelected = selectedCrypto == crypto.symbol,
                    onClick = { selectedCrypto = crypto.symbol }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Amount Input
        Text(
            text = "Amount to Buy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                amountError = if (it.isEmpty()) "Amount is required" else ""
            },
            label = { Text("Enter amount") },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = amountError.isNotEmpty(),
            supportingText = if (amountError.isNotEmpty()) {
                { Text(amountError, color = MaterialTheme.colorScheme.error) }
            } else null,
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Amount") }
        )
        
        // Buy Button
        Button(
            onClick = { /* Handle buy */ },
            enabled = amount.isNotEmpty() && amountError.isEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = "Buy ${selectedCrypto}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SellCryptoScreen() {
    var selectedCrypto by remember { mutableStateOf("BTC") }
    var amount by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Crypto Selection
        Text(
            text = "Select Cryptocurrency",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getCryptoList()) { crypto ->
                CryptoSelectionCard(
                    crypto = crypto,
                    isSelected = selectedCrypto == crypto.symbol,
                    onClick = { selectedCrypto = crypto.symbol }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Amount Input
        Text(
            text = "Amount to Sell",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                amountError = if (it.isEmpty()) "Amount is required" else ""
            },
            label = { Text("Enter amount") },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = amountError.isNotEmpty(),
            supportingText = if (amountError.isNotEmpty()) {
                { Text(amountError, color = MaterialTheme.colorScheme.error) }
            } else null,
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Amount") }
        )
        
        // Sell Button
        Button(
            onClick = { /* Handle sell */ },
            enabled = amount.isNotEmpty() && amountError.isEmpty(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "Sell ${selectedCrypto}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CryptoSelectionCard(
    crypto: CryptoCurrency,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            crypto.color,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = crypto.symbol,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = crypto.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = crypto.symbol,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${crypto.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${crypto.change}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (crypto.change >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

data class CryptoCurrency(
    val name: String,
    val symbol: String,
    val price: String,
    val change: Double,
    val color: Color
)

fun getCryptoList(): List<CryptoCurrency> = listOf(
    CryptoCurrency(
        name = "Bitcoin",
        symbol = "BTC",
        price = "45,230.50",
        change = 2.5,
        color = Color(0xFFF7931A)
    ),
    CryptoCurrency(
        name = "Ethereum",
        symbol = "ETH",
        price = "3,120.75",
        change = -1.2,
        color = Color(0xFF627EEA)
    ),
    CryptoCurrency(
        name = "Cardano",
        symbol = "ADA",
        price = "0.85",
        change = 5.8,
        color = Color(0xFF0033AD)
    ),
    CryptoCurrency(
        name = "Solana",
        symbol = "SOL",
        price = "98.45",
        change = 3.2,
        color = Color(0xFF9945FF)
    ),
    CryptoCurrency(
        name = "Polygon",
        symbol = "MATIC",
        price = "0.92",
        change = -2.1,
        color = Color(0xFF8247E5)
    )
)
