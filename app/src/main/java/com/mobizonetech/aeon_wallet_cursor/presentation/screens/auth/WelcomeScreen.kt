package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onApplyForLoanCardClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    val coroutineScope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Page Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .background(
                                color = if (isSelected) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                    )
                }
            }
            
            // Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                WelcomeSlide(
                    slideData = getWelcomeSlides()[page],
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Bottom Actions
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Skip/Next Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage < 4) {
                        TextButton(
                            onClick = { 
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(4)
                                }
                            }
                        ) {
                        Text(stringResource(R.string.skip))
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    if (pagerState.currentPage < 4) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(stringResource(R.string.next))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Get Started Button (only on last slide)
                if (pagerState.currentPage == 4) {
                    // Apply for Loan/Card Button
                    OutlinedButton(
                        onClick = onApplyForLoanCardClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.apply_for_loan_card),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onGetStartedClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.get_started),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sign In Button
                    OutlinedButton(
                        onClick = onSignInClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeSlide(
    slideData: WelcomeSlideData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Card(
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = slideData.iconBackgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slideData.icon,
                    fontSize = 48.sp,
                    color = slideData.iconColor
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Title
        Text(
            text = slideData.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        Text(
            text = slideData.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Features
        slideData.features.forEach { feature ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class WelcomeSlideData(
    val title: String,
    val description: String,
    val icon: String,
    val iconBackgroundColor: Color,
    val iconColor: Color,
    val features: List<String>
)

fun getWelcomeSlides(): List<WelcomeSlideData> = listOf(
    WelcomeSlideData(
        title = "Welcome to Aeon Wallet",
        description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
        icon = "₿",
        iconBackgroundColor = androidx.compose.ui.graphics.Color(0xFF6200EE),
        iconColor = androidx.compose.ui.graphics.Color.White,
        features = listOf(
            "Bank-level security",
            "Instant transactions",
            "24/7 support"
        )
    ),
    WelcomeSlideData(
        title = "Secure & Fast",
        description = "Send and receive money instantly with military-grade encryption protecting your transactions.",
        icon = "🔒",
        iconBackgroundColor = androidx.compose.ui.graphics.Color(0xFFB00020),
        iconColor = androidx.compose.ui.graphics.Color.White,
        features = listOf(
            "End-to-end encryption",
            "Instant transfers",
            "Fraud protection"
        )
    ),
    WelcomeSlideData(
        title = "Crypto Trading",
        description = "Trade cryptocurrencies with zero fees and real-time market data at your fingertips.",
        icon = "📈",
        iconBackgroundColor = androidx.compose.ui.graphics.Color(0xFF03DAC6),
        iconColor = androidx.compose.ui.graphics.Color.White,
        features = listOf(
            "Zero trading fees",
            "Real-time prices",
            "Multiple cryptocurrencies"
        )
    ),
    WelcomeSlideData(
        title = "Smart Analytics",
        description = "Track your spending, set budgets, and get insights to make smarter financial decisions.",
        icon = "📊",
        iconBackgroundColor = androidx.compose.ui.graphics.Color(0xFF018786),
        iconColor = androidx.compose.ui.graphics.Color.White,
        features = listOf(
            "Spending insights",
            "Budget tracking",
            "Financial reports"
        )
    ),
    WelcomeSlideData(
        title = "Ready to Start?",
        description = "Join thousands of users who trust Aeon Wallet for their digital finance needs.",
        icon = "🚀",
        iconBackgroundColor = androidx.compose.ui.graphics.Color(0xFF6200EE),
        iconColor = androidx.compose.ui.graphics.Color.White,
        features = listOf(
            "Quick setup in minutes",
            "No hidden fees",
            "Global accessibility"
        )
    )
)

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    AeonwalletcursorTheme {
        WelcomeScreen()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WelcomeScreenDarkPreview() {
    AeonwalletcursorTheme {
        WelcomeScreen()
    }
}
