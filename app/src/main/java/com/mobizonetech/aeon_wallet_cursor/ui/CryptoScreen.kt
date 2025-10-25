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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoScreen(
    onTradingClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto Portfolio") },
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
            
            // Portfolio Summary
            item {
                PortfolioSummaryCard()
            }
            
            // Crypto Assets Section
            item {
                Text(
                    text = "My Crypto Assets",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            items(getCryptoAssetsList()) { asset ->
                CryptoAssetCard(asset = asset)
            }
            
            // Market Section
            item {
                Text(
                    text = "Market Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }
            
            items(getMarketAssetsList()) { asset ->
                MarketAssetCard(
                    asset = asset,
                    onClick = onTradingClick
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun PortfolioSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Portfolio Value",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$12,450.75",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+$1,234.50 (+11.02%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CryptoAssetCard(asset: CryptoScreenAsset) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                asset.icon,
                contentDescription = null,
                tint = asset.color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = asset.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = asset.value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = asset.change,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (asset.isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun MarketAssetCard(
    asset: CryptoScreenAsset,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                asset.icon,
                contentDescription = null,
                tint = asset.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = asset.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = asset.value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = asset.change,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (asset.isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

data class CryptoScreenAsset(
    val name: String,
    val symbol: String,
    val value: String,
    val change: String,
    val icon: ImageVector,
    val color: Color,
    val isPositive: Boolean
)

fun getCryptoAssetsList(): List<CryptoScreenAsset> {
    return listOf(
        CryptoScreenAsset(
            name = "Bitcoin",
            symbol = "BTC",
            value = "$8,450.25",
            change = "+$1,234.50 (+17.1%)",
            icon = Icons.Default.Star,
            color = Color(0xFFF7931A),
            isPositive = true
        ),
        CryptoScreenAsset(
            name = "Ethereum",
            symbol = "ETH",
            value = "$2,340.75",
            change = "+$156.25 (+7.2%)",
            icon = Icons.Default.Star,
            color = Color(0xFF627EEA),
            isPositive = true
        ),
        CryptoScreenAsset(
            name = "Cardano",
            symbol = "ADA",
            value = "$1,890.50",
            change = "-$45.75 (-2.4%)",
            icon = Icons.Default.Star,
            color = Color(0xFF0033AD),
            isPositive = false
        ),
        CryptoScreenAsset(
            name = "Solana",
            symbol = "SOL",
            value = "$1,670.00",
            change = "+$89.25 (+5.6%)",
            icon = Icons.Default.Star,
            color = Color(0xFF9945FF),
            isPositive = true
        )
    )
}

fun getMarketAssetsList(): List<CryptoScreenAsset> {
    return listOf(
        CryptoScreenAsset(
            name = "Polygon",
            symbol = "MATIC",
            value = "$0.85",
            change = "+$0.12 (+16.4%)",
            icon = Icons.Default.Star,
            color = Color(0xFF8247E5),
            isPositive = true
        ),
        CryptoScreenAsset(
            name = "Chainlink",
            symbol = "LINK",
            value = "$14.25",
            change = "-$0.85 (-5.6%)",
            icon = Icons.Default.Star,
            color = Color(0xFF2A5ADA),
            isPositive = false
        ),
        CryptoScreenAsset(
            name = "Polkadot",
            symbol = "DOT",
            value = "$6.45",
            change = "+$0.35 (+5.7%)",
            icon = Icons.Default.Star,
            color = Color(0xFFE6007A),
            isPositive = true
        )
    )
}
