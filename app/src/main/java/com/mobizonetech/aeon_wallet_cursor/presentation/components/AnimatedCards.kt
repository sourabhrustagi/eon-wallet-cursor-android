package com.mobizonetech.aeon_wallet_cursor.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.model.CardBrand

@Composable
fun AnimatedCard(
    card: Card,
    onClick: () -> Unit,
    onUnlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_elevation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .scale(scale)
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = Color(card.backgroundColor)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(card.backgroundColor),
                            Color(card.backgroundColor).copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            // Card content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with unlock button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.title,
                        color = Color(card.textColor),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    AnimatedUnlockButton(
                        isLocked = card.isLocked,
                        onClick = onUnlockClick
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = card.subtitle,
                    color = Color(card.textColor).copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Card number
                Text(
                    text = card.maskedCardNumber,
                    color = Color(card.textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = card.cardHolderName,
                            color = Color(card.textColor),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = card.expiryDate,
                            color = Color(card.textColor).copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                    
                    Text(
                        text = card.amount,
                        color = Color(card.textColor),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }
            
            // Card brand icon
            CardBrandIcon(
                brand = card.cardBrand,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun AnimatedUnlockButton(
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon by animateContentAsState(
        targetState = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "unlock_icon"
    )
    
    val color by animateColorAsState(
        targetValue = if (isLocked) Color.Red else Color.Green,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "unlock_color"
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isLocked) "Unlock" else "Lock",
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun CardBrandIcon(
    brand: CardBrand,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (brand) {
        CardBrand.VISA -> Icons.Default.CreditCard
        CardBrand.MASTERCARD -> Icons.Default.CreditCard
        CardBrand.AMEX -> Icons.Default.CreditCard
    }
    
    val color: Color = when (brand) {
        CardBrand.VISA -> Color.Blue
        CardBrand.MASTERCARD -> Color.Red
        CardBrand.AMEX -> Color.Green
    }

    Icon(
        imageVector = icon,
        contentDescription = brand.name,
        tint = color,
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun AnimatedLoanCard(
    loan: com.mobizonetech.aeon_wallet_cursor.domain.model.Loan,
    onClick: () -> Unit,
    onUnlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "loan_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .scale(scale)
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(loan.backgroundColor)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Loan icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(loan.textColor).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Loan",
                    tint = Color(loan.textColor),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = loan.title,
                    color = Color(loan.textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = loan.subtitle,
                    color = Color(loan.textColor).copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = loan.amount,
                    color = Color(loan.textColor),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                AnimatedUnlockButton(
                    isLocked = loan.isLocked,
                    onClick = onUnlockClick
                )
            }
        }
    }
}
