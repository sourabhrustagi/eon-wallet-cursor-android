package com.mobizonetech.aeon_wallet_cursor.presentation.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.WelcomeViewModel
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = viewModel()
) {
    val slides by viewModel.slides.collectAsState()

    if (slides.isEmpty()) {
        // Show loading or empty state
        return
    }

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6200EE),
                        Color(0xFF3700B3)
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
                slides.forEachIndexed { index, _ ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .background(
                                color = if (isSelected) 
                                    Color.White 
                                else 
                                    Color.White.copy(alpha = 0.3f),
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
                    slide = slides[page],
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
                    if (pagerState.currentPage < slides.size - 1) {
                        TextButton(
                            onClick = { 
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(slides.size - 1)
                                }
                            }
                        ) {
                            Text("Skip", color = Color.White)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }

                    if (pagerState.currentPage < slides.size - 1) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text("Next", color = Color(0xFF6200EE))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Get Started Button (only on last slide)
                if (pagerState.currentPage == slides.size - 1) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6200EE)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(listOf(Color.White, Color.White))
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Sign In",
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
    slide: WelcomeSlide,
    modifier: Modifier = Modifier
) {
    val iconColor = Color(slide.iconBackgroundColor)
    
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
                containerColor = Color.White.copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slide.icon,
                    fontSize = 64.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = slide.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = slide.description,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Features
        slide.features.forEach { feature ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = feature,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

