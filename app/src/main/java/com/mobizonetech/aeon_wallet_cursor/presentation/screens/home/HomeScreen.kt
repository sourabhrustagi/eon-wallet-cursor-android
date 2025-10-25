package com.mobizonetech.aeon_wallet_cursor.presentation.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.domain.model.CardBrand
import com.mobizonetech.aeon_wallet_cursor.presentation.components.*
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.HomeViewModel
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "John Doe",
    onPromotionClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onBankProductsClick: () -> Unit = {},
    onCardApplyClick: (String) -> Unit = {},
    onPointsClick: () -> Unit = {},
    onUtilityBillClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val cards by viewModel.combinedCards.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    LaunchedEffect(error) {
        if (error != null) {
            // Handle error - could show snackbar
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Header
            item {
                AnimatedCard(
                    onClick = onPromotionClick
                ) {
                    WelcomeHeader(userName = userName)
                }
            }
            
            // Cards Section
            item {
                Text(
                    text = stringResource(R.string.my_cards),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            if (isLoading) {
                item {
                    LoadingScreen()
                }
            } else {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(cards) { card ->
                            AnimatedCard(
                                card = card,
                                onClick = { onCardClick(card.id) },
                                onUnlockClick = { viewModel.unlockCard(card.id) }
                            )
                        }
                    }
                }
            }
            
            // Quick Actions
            item {
                Text(
                    text = stringResource(R.string.quick_actions),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            item {
                QuickActionsGrid(
                    onBankProductsClick = onBankProductsClick,
                    onCardApplyClick = onCardApplyClick,
                    onPointsClick = onPointsClick,
                    onUtilityBillClick = onUtilityBillClick
                )
            }
        }
    }
}
