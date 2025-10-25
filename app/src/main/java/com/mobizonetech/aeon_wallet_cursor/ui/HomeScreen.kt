package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "John Doe",
    onPromotionClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onBankProductsClick: () -> Unit = {},
    onCardApplyClick: (String) -> Unit = {},
    onPointsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val unlockedCards by unlockPreferences.unlockedCards.collectAsState(initial = emptySet())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome back,",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = userName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Points Section
            item {
                PointsSection(
                    onPointsClick = onPointsClick
                )
            }
            
            // Cards Section (Combined) - Moved to top
            item {
                CombinedCardsSection(
                    onCardClick = { cardId -> onCardClick(cardId) },
                    unlockedCards = unlockedCards
                )
            }
            
            // Promotions Section
            item {
                PromotionsSection(
                    onPromotionClick = onPromotionClick
                )
            }
            
            // Bank Products Section
            item {
                BankProductsSection(
                    onBankProductsClick = onBankProductsClick,
                    onCardApplyClick = onCardApplyClick
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun PromotionsSection(
    onPromotionClick: () -> Unit
) {
    Column {
        Text(
            text = "Promotions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            getPromotions().forEach { promotion ->
                PromotionCard(
                    promotion = promotion,
                    onClick = onPromotionClick
                )
            }
        }
    }
}

@Composable
fun PromotionCard(
    promotion: Promotion,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = promotion.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = promotion.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = promotion.textColor,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = promotion.description,
                            fontSize = 12.sp,
                            color = promotion.textColor.copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                    Icon(
                        imageVector = promotion.icon,
                        contentDescription = null,
                        tint = promotion.textColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Text(
                    text = promotion.actionText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = promotion.textColor
                )
            }
        }
    }
}

@Composable
fun CombinedCardsSection(
    onCardClick: (String) -> Unit,
    unlockedCards: Set<String>
) {
    val allCards = getCards()
    val unlockedCardsList = allCards.filter { unlockedCards.contains(it.id) }
    val lockedCardsList = allCards.filter { !unlockedCards.contains(it.id) }
    
    // Combine lists with unlocked cards first
    val combinedCards = unlockedCardsList + lockedCardsList
    
    if (combinedCards.isNotEmpty()) {
        Column {
            Text(
                text = "My Cards",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                combinedCards.forEach { item ->
                    RealisticCardView(
                        card = item,
                        unlockedCards = unlockedCards,
                        onClick = { onCardClick(item.id) }
                    )
                }
            }
        }
    }
}

data class CardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color,
    val isLocked: Boolean = false,
    val cardNumber: String = "",
    val maskedCardNumber: String = "",
    val cardHolderName: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val cardBrand: CardBrand = CardBrand.VISA
)

enum class CardBrand {
    VISA, MASTERCARD, AMEX, DISCOVER
}

fun getCards(): List<CardItem> = listOf(
    CardItem(
        id = "card_1",
        title = "Credit Card",
        subtitle = "Available Credit",
        amount = "$5,000",
        icon = Icons.Default.Star,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF1A1A2E),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = true,
        cardNumber = "4532123456789012",
        maskedCardNumber = "4532 **** **** 9012",
        cardHolderName = "JOHN DOE",
        expiryDate = "12/25",
        cvv = "123",
        cardBrand = CardBrand.VISA
    ),
    CardItem(
        id = "card_2",
        title = "Debit Card",
        subtitle = "Available Balance",
        amount = "$2,500",
        icon = Icons.Default.Star,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF0F3460),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = false,
        cardNumber = "5555123456789012",
        maskedCardNumber = "5555 **** **** 9012",
        cardHolderName = "JOHN DOE",
        expiryDate = "08/26",
        cvv = "456",
        cardBrand = CardBrand.MASTERCARD
    ),
    CardItem(
        id = "card_3",
        title = "Savings Card",
        subtitle = "Available Balance",
        amount = "$8,750",
        icon = Icons.Default.Star,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF2D1B69),
        textColor = androidx.compose.ui.graphics.Color.White,
        isLocked = false,
        cardNumber = "4000123456789012",
        maskedCardNumber = "4000 **** **** 9012",
        cardHolderName = "JOHN DOE",
        expiryDate = "03/27",
        cvv = "789",
        cardBrand = CardBrand.VISA
    )
)

@Composable
fun BankProductsSection(
    onBankProductsClick: () -> Unit,
    onCardApplyClick: (String) -> Unit = {}
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
            // Premium Card
            EnhancedCardItem(
                title = "Premium Card",
                subtitle = "Up to $50,000",
                highlight = "5% Cashback",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF1A237E),
                onClick = { onCardApplyClick("premium_card") }
            )
            
            // Privilege Card
            EnhancedCardItem(
                title = "Privilege Card",
                subtitle = "Up to $75,000",
                highlight = "7% Cashback",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF8E24AA),
                onClick = { onCardApplyClick("privilege_card") }
            )
            
            // Super Mart Card
            EnhancedCardItem(
                title = "Super Mart Card",
                subtitle = "Up to $30,000",
                highlight = "4% Grocery",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF2E7D32),
                onClick = { onCardApplyClick("super_mart_card") }
            )
            
            // Travel Card
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(120.dp),
                onClick = { onCardApplyClick("travel_card") },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFE65100)
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
                            text = "Travel Card",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
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
                            text = "Up to $35,000",
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "3x Points",
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

data class Promotion(
    val title: String,
    val description: String,
    val actionText: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color
)

fun getPromotions(): List<Promotion> = listOf(
    Promotion(
        title = "Cashback Rewards",
        description = "Get 5% cashback on all purchases",
        actionText = "Activate Now",
        icon = Icons.Default.Star,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
        textColor = androidx.compose.ui.graphics.Color.White
    ),
    Promotion(
        title = "Investment Bonus",
        description = "Earn extra 2% on your investments",
        actionText = "Learn More",
        icon = Icons.Default.Star,
        backgroundColor = androidx.compose.ui.graphics.Color(0xFF2196F3),
        textColor = androidx.compose.ui.graphics.Color.White
    )
)

@Composable
fun UnlockedCardsSection(
    onCardClick: (String) -> Unit,
    unlockedCards: Set<String>
) {
    val unlockedCardsList = getCards().filter { unlockedCards.contains(it.id) }
    
    if (unlockedCardsList.isNotEmpty()) {
        Column {
            Text(
                text = "Unlocked Cards",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                unlockedCardsList.forEach { item ->
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .height(120.dp),
                        onClick = { onCardClick(item.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = item.backgroundColor
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
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = item.textColor
                                )
                                Icon(
                                    item.icon,
                                    contentDescription = null,
                                    tint = item.textColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Column {
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = item.textColor
                                )
                                Text(
                                    text = item.amount,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = item.textColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PointsSection(
    onPointsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onPointsClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Reward Points",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "2,450 points",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap to redeem",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                Icons.Default.Star,
                contentDescription = "Points",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun RealisticCardView(
    card: CardItem,
    unlockedCards: Set<String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (card.isLocked && !unlockedCards.contains(card.id)) 
                card.backgroundColor.copy(alpha = 0.4f) 
            else card.backgroundColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        // Lock overlay for locked cards
        if (card.isLocked && !unlockedCards.contains(card.id)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
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
                        text = card.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        
        // Card content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            card.backgroundColor,
                            card.backgroundColor.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row - Bank name and card brand
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AEON BANK",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = card.textColor,
                        letterSpacing = 1.sp
                    )
                    
                    // Card brand logo
                    CardBrandLogo(cardBrand = card.cardBrand)
                }
                
                // Card number
                Column {
                    Text(
                        text = if (unlockedCards.contains(card.id)) card.maskedCardNumber else "**** **** **** ****",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = card.textColor,
                        letterSpacing = 2.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Card holder name and expiry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "CARDHOLDER NAME",
                                style = MaterialTheme.typography.bodySmall,
                                color = card.textColor.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = if (unlockedCards.contains(card.id)) card.cardHolderName else "**** ****",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = card.textColor,
                                letterSpacing = 1.sp
                            )
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "EXPIRES",
                                style = MaterialTheme.typography.bodySmall,
                                color = card.textColor.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = if (unlockedCards.contains(card.id)) card.expiryDate else "**/**",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = card.textColor,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
                
                // Bottom row - CVV and balance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CVV: ${if (unlockedCards.contains(card.id)) card.cvv else "***"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = card.textColor.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    
                    Text(
                        text = card.amount,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = card.textColor
                    )
                }
            }
        }
    }
}

@Composable
fun CardBrandLogo(cardBrand: CardBrand) {
    when (cardBrand) {
        CardBrand.VISA -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VISA",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1F71),
                    fontSize = 10.sp
                )
            }
        }
        CardBrand.MASTERCARD -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MC",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEB001B),
                    fontSize = 10.sp
                )
            }
        }
        CardBrand.AMEX -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AMEX",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006FCF),
                    fontSize = 8.sp
                )
            }
        }
        CardBrand.DISCOVER -> {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DISC",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6000),
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun EnhancedCardItem(
    title: String,
    subtitle: String,
    highlight: String,
    icon: ImageVector,
    backgroundColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            backgroundColor,
                            backgroundColor.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section with title and icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                // Bottom section with highlight
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = highlight,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontSize = 16.sp
                    )
                    
                    Box(
                        modifier = Modifier
                            .background(
                                androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Apply",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}