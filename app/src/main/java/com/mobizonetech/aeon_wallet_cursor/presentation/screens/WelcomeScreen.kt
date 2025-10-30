package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.WelcomeUiState
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.WelcomeViewModel
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import com.mobizonetech.aeon_wallet_cursor.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Welcome/Onboarding Screen
 * Displays a horizontal pager with welcome slides
 * 
 * Features:
 * - Horizontal paging with indicators
 * - Skip and Next navigation
 * - Get Started and Sign In actions on final page
 * - Gradient background
 * - Loading and error states
 * 
 * @param viewModel ViewModel for managing welcome screen state
 */
@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {}
) {
    // Callbacks passed from navigation
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = 0, 
        pageCount = { uiState.slides.size }
    )
    val coroutineScope = rememberCoroutineScope()

    // Show loading state
    if (uiState.isLoading) {
        LoadingState()
        return
    }

    // Show error state
    if (uiState.error != null || uiState.slides.isEmpty()) {
        ErrorState(error = uiState.error)
        return
    }

    // Sync pager with ViewModel current page
    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    // Update ViewModel when pager changes
    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != uiState.currentPage) {
                viewModel.onPageChanged(page)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(Constants.GRADIENT_START),
                        Color(Constants.GRADIENT_END)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Page Indicator
            PageIndicators(
                pageCount = uiState.slides.size,
                currentPage = uiState.currentPage
            )

            // Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                WelcomeSlide(
                    slide = uiState.slides[page],
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bottom Actions
            BottomActionButtons(
                uiState = uiState,
                viewModel = viewModel,
                pagerState = pagerState,
                coroutineScope = coroutineScope
            )
        }
    }
}

/**
 * Loading state composable
 * Shows a centered circular progress indicator
 * 
 * @param modifier Modifier for the loading state container (defaults to fillMaxSize)
 */
@Composable
internal fun LoadingState(modifier: Modifier = Modifier.fillMaxSize()) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(Constants.GRADIENT_START),
                        Color(Constants.GRADIENT_END)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 4.dp
        )
    }
}

/**
 * Error state composable
 * Shows an error message to the user
 * 
 * @param error Error message to display, or null for default message
 * @param modifier Modifier for the error state container (defaults to fillMaxSize)
 */
@Composable
internal fun ErrorState(
    error: String?,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val errorMessage = error ?: stringResource(R.string.welcome_error_no_slides)
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(Constants.GRADIENT_START),
                        Color(Constants.GRADIENT_END)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(Constants.SPACING_LARGE.dp)
        )
    }
}

/**
 * Page indicators composable
 * Shows dots representing each page with current page highlighted
 * 
 * @param pageCount Total number of pages
 * @param currentPage Current active page index
 * @param modifier Optional modifier for customization
 */
@Composable
internal fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Constants.SPACING_MEDIUM.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index
            Box(
                modifier = Modifier
                    .padding(horizontal = Constants.SPACING_SMALL.dp)
                    .size(
                        if (isSelected) 
                            Constants.PAGE_INDICATOR_SELECTED_SIZE.dp 
                        else 
                            Constants.PAGE_INDICATOR_UNSELECTED_SIZE.dp
                    )
                    .background(
                        color = if (isSelected) 
                            Color.White 
                        else 
                            Color.White.copy(alpha = Constants.ALPHA_TRANSPARENT),
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * Bottom action buttons composable
 * Shows Skip/Next buttons and Get Started/Sign In buttons on last page
 * 
 * @param uiState Current UI state
 * @param viewModel ViewModel for handling button actions
 * @param pagerState Pager state for controlling page navigation
 * @param coroutineScope Coroutine scope for launching animations
 */
@Composable
private fun BottomActionButtons(
    uiState: WelcomeUiState,
    viewModel: WelcomeViewModel,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier.padding(Constants.SPACING_LARGE.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Skip/Next Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (uiState.canNavigateSkip) {
                TextButton(
                    onClick = { 
                        viewModel.onSkipClick()
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(uiState.slides.size - 1)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.welcome_skip),
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(Constants.BUTTON_HEIGHT.dp))
            }

            if (uiState.canNavigateNext) {
                Button(
                    onClick = {
                        viewModel.onNextClick()
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(uiState.currentPage + 1)
                        }
                    },
                    shape = RoundedCornerShape(Constants.BUTTON_CORNER_RADIUS.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.welcome_next),
                        color = Color(Constants.PRIMARY_COLOR)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Constants.SPACING_MEDIUM.dp))

        // Get Started Button (only on last slide)
        if (uiState.isOnLastPage) {
            Button(
                onClick = {
                    viewModel.onGetStartedClick()
//                    onGetStarted()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Constants.BUTTON_HEIGHT.dp),
                shape = RoundedCornerShape(Constants.BUTTON_CORNER_RADIUS.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = Constants.ELEVATION_DEFAULT.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.welcome_get_started),
                    fontSize = Constants.TEXT_SIZE_MEDIUM.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(Constants.PRIMARY_COLOR)
                )
            }

            Spacer(modifier = Modifier.height(Constants.SPACING_MEDIUM.dp))

            OutlinedButton(
                onClick = {
                    viewModel.onSignInClick()
//                    onSignIn()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Constants.BUTTON_HEIGHT.dp),
                shape = RoundedCornerShape(Constants.BUTTON_CORNER_RADIUS.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = Constants.BUTTON_BORDER_WIDTH.dp,
                    brush = Brush.linearGradient(listOf(Color.White, Color.White))
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.welcome_sign_in),
                    fontSize = Constants.TEXT_SIZE_MEDIUM.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Individual welcome slide composable
 * Displays icon, title, description, and features for a single slide
 * 
 * @param slide Slide data to display
 * @param modifier Optional modifier for customization
 */
@Composable
fun WelcomeSlide(
    slide: WelcomeSlide,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(Constants.SPACING_LARGE.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        WelcomeIcon(slide = slide)

        Spacer(modifier = Modifier.height(Constants.SPACING_EXTRA_LARGE.dp))

        // Title
        WelcomeTitle(title = slide.title)

        Spacer(modifier = Modifier.height(Constants.SPACING_MEDIUM.dp))

        // Description
        WelcomeDescription(description = slide.description)

        Spacer(modifier = Modifier.height(Constants.SPACING_LARGE.dp))

        // Features
        WelcomeFeatures(features = slide.features)
    }
}

/**
 * Welcome icon composable
 * Displays the slide icon in a styled card
 * 
 * @param slide Slide containing icon data
 */
@Composable
internal fun WelcomeIcon(slide: WelcomeSlide) {
    Card(
        modifier = Modifier.size(Constants.ICON_SIZE.dp),
        shape = RoundedCornerShape(Constants.CARD_CORNER_RADIUS.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = Constants.ALPHA_BACKGROUND)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Constants.ELEVATION_DEFAULT.dp + 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = slide.icon,
                fontSize = Constants.TEXT_SIZE_ICON.sp,
                color = Color.White
            )
        }
    }
}

/**
 * Welcome title composable
 * Displays the slide title with consistent styling
 * 
 * @param title Title text to display
 */
@Composable
internal fun WelcomeTitle(title: String) {
    Text(
        text = title,
        fontSize = Constants.TEXT_SIZE_LARGE.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

/**
 * Welcome description composable
 * Displays the slide description with consistent styling
 * 
 * @param description Description text to display
 */
@Composable
internal fun WelcomeDescription(description: String) {
    Text(
        text = description,
        fontSize = Constants.TEXT_SIZE_NORMAL.sp,
        color = Color.White.copy(alpha = Constants.ALPHA_SEMI_TRANSPARENT),
        textAlign = TextAlign.Center,
        lineHeight = Constants.SPACING_LARGE.sp
    )
}

/**
 * Welcome features composable
 * Displays a list of features with checkmarks
 * 
 * @param features List of feature strings to display
 */
@Composable
internal fun WelcomeFeatures(features: List<String>) {
    val checkmark = stringResource(R.string.welcome_checkmark)
    
    features.forEach { feature ->
        Row(
            modifier = Modifier.padding(vertical = Constants.SPACING_SMALL.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = checkmark,
                fontSize = Constants.TEXT_SIZE_CHECK.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = feature,
                fontSize = Constants.TEXT_SIZE_NORMAL.sp,
                color = Color.White.copy(alpha = Constants.ALPHA_SEMI_TRANSPARENT)
            )
        }
    }
}

// =============================================================================
// Preview Functions
// =============================================================================

/**
 * Preview parameter provider for WelcomeSlide
 */
class WelcomeSlidePreviewProvider : PreviewParameterProvider<WelcomeSlide> {
    override val values = sequenceOf(
        WelcomeSlide(
            id = 0,
            title = "Welcome to Aeon Wallet",
            description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
            icon = "₿",
            iconBackgroundColor = 0xFF6200EE,
            features = listOf(
                "Bank-level security",
                "Instant transactions",
                "24/7 support"
            )
        ),
        WelcomeSlide(
            id = 1,
            title = "Secure & Fast",
            description = "Send and receive money instantly with military-grade encryption protecting your transactions.",
            icon = "🔒",
            iconBackgroundColor = 0xFFB00020,
            features = listOf(
                "End-to-end encryption",
                "Instant transfers",
                "Fraud protection"
            )
        ),
        WelcomeSlide(
            id = 2,
            title = "Crypto Trading",
            description = "Trade cryptocurrencies with zero fees and real-time market data at your fingertips.",
            icon = "📈",
            iconBackgroundColor = 0xFF03DAC6,
            features = listOf(
                "Zero trading fees",
                "Real-time prices",
                "Multiple cryptocurrencies"
            )
        )
    )
}

/**
 * Preview for LoadingState
 */
@Preview(
    name = "Loading State - Light",
    showBackground = true,
    backgroundColor = 0xFF6200EE
)
@Preview(
    name = "Loading State - Dark",
    showBackground = true,
    backgroundColor = 0xFF3700B3,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LoadingStatePreview() {
    AeonwalletcursorTheme {
        LoadingState()
    }
}

/**
 * Preview for ErrorState
 */
@Preview(
    name = "Error State - With Message",
    showBackground = true
)
@Composable
private fun ErrorStatePreview() {
    AeonwalletcursorTheme {
        ErrorState(error = "Failed to load welcome slides. Please try again.")
    }
}

/**
 * Preview for ErrorState with null error
 */
@Preview(
    name = "Error State - Default Message",
    showBackground = true
)
@Composable
private fun ErrorStateDefaultPreview() {
    AeonwalletcursorTheme {
        ErrorState(error = null)
    }
}

/**
 * Preview for PageIndicators
 */
@Preview(
    name = "Page Indicators - First Page",
    showBackground = true
)
@Composable
private fun PageIndicatorsFirstPagePreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            PageIndicators(
                pageCount = 5,
                currentPage = 0
            )
        }
    }
}

/**
 * Preview for PageIndicators - Middle page
 */
@Preview(
    name = "Page Indicators - Middle Page",
    showBackground = true
)
@Composable
private fun PageIndicatorsMiddlePagePreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            PageIndicators(
                pageCount = 5,
                currentPage = 2
            )
        }
    }
}

/**
 * Preview for PageIndicators - Last page
 */
@Preview(
    name = "Page Indicators - Last Page",
    showBackground = true
)
@Composable
private fun PageIndicatorsLastPagePreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            PageIndicators(
                pageCount = 5,
                currentPage = 4
            )
        }
    }
}

/**
 * Preview for WelcomeSlide with different slides
 */
@Preview(
    name = "Welcome Slide - First",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun WelcomeSlideFirstPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(
                slide = WelcomeSlide(
                    id = 0,
                    title = "Welcome to Aeon Wallet",
                    description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
                    icon = "₿",
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        "Bank-level security",
                        "Instant transactions",
                        "24/7 support"
                    )
                )
            )
        }
    }
}

/**
 * Preview for WelcomeSlide - Crypto Trading
 */
@Preview(
    name = "Welcome Slide - Crypto",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun WelcomeSlideCryptoPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(
                slide = WelcomeSlide(
                    id = 2,
                    title = "Crypto Trading",
                    description = "Trade cryptocurrencies with zero fees and real-time market data at your fingertips.",
                    icon = "📈",
                    iconBackgroundColor = 0xFF03DAC6,
                    features = listOf(
                        "Zero trading fees",
                        "Real-time prices",
                        "Multiple cryptocurrencies"
                    )
                )
            )
        }
    }
}

/**
 * Preview for WelcomeIcon
 */
@Preview(
    name = "Welcome Icon - Bitcoin",
    showBackground = true
)
@Composable
private fun WelcomeIconBitcoinPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            WelcomeIcon(
                slide = WelcomeSlide(
                    id = 0,
                    title = "Welcome",
                    description = "Description",
                    icon = "₿",
                    iconBackgroundColor = 0xFF6200EE,
                    features = emptyList()
                )
            )
        }
    }
}

/**
 * Preview for WelcomeIcon - Lock
 */
@Preview(
    name = "Welcome Icon - Lock",
    showBackground = true
)
@Composable
private fun WelcomeIconLockPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            WelcomeIcon(
                slide = WelcomeSlide(
                    id = 1,
                    title = "Secure",
                    description = "Description",
                    icon = "🔒",
                    iconBackgroundColor = 0xFFB00020,
                    features = emptyList()
                )
            )
        }
    }
}

/**
 * Preview for WelcomeTitle
 */
@Preview(
    name = "Welcome Title",
    showBackground = true
)
@Composable
private fun WelcomeTitlePreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            WelcomeTitle(title = "Welcome to Aeon Wallet")
        }
    }
}

/**
 * Preview for WelcomeDescription
 */
@Preview(
    name = "Welcome Description",
    showBackground = true
)
@Composable
private fun WelcomeDescriptionPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            WelcomeDescription(
                description = "Your secure gateway to digital finance. Manage your money with confidence and ease."
            )
        }
    }
}

/**
 * Preview for WelcomeFeatures
 */
@Preview(
    name = "Welcome Features",
    showBackground = true
)
@Composable
private fun WelcomeFeaturesPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(Constants.GRADIENT_START))
                .padding(16.dp)
        ) {
            WelcomeFeatures(
                features = listOf(
                    "Bank-level security",
                    "Instant transactions",
                    "24/7 support"
                )
            )
        }
    }
}

/**
 * Preview for full WelcomeScreen with multiple states
 */
@Preview(
    name = "Welcome Slide - Parameterized",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun WelcomeSlideParameterizedPreview(
    @PreviewParameter(WelcomeSlidePreviewProvider::class) slide: WelcomeSlide
) {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(slide = slide)
        }
    }
}

/**
 * Preview for WelcomeSlide in Dark Mode
 */
@Preview(
    name = "Welcome Slide - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WelcomeSlideDarkPreview() {
    AeonwalletcursorTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(
                slide = WelcomeSlide(
                    id = 0,
                    title = "Welcome to Aeon Wallet",
                    description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
                    icon = "₿",
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        "Bank-level security",
                        "Instant transactions",
                        "24/7 support"
                    )
                )
            )
        }
    }
}

/**
 * Preview for different screen sizes
 */
@Preview(
    name = "Welcome Slide - Small Screen",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun WelcomeSlideSmallScreenPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(
                slide = WelcomeSlide(
                    id = 0,
                    title = "Welcome to Aeon Wallet",
                    description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
                    icon = "₿",
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        "Bank-level security",
                        "Instant transactions",
                        "24/7 support"
                    )
                )
            )
        }
    }
}

/**
 * Preview for tablet/large screen
 */
@Preview(
    name = "Welcome Slide - Tablet",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
private fun WelcomeSlideTabletPreview() {
    AeonwalletcursorTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(Constants.GRADIENT_START),
                            Color(Constants.GRADIENT_END)
                        )
                    )
                )
        ) {
            WelcomeSlide(
                slide = WelcomeSlide(
                    id = 0,
                    title = "Welcome to Aeon Wallet",
                    description = "Your secure gateway to digital finance. Manage your money with confidence and ease.",
                    icon = "₿",
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        "Bank-level security",
                        "Instant transactions",
                        "24/7 support"
                    )
                )
            )
        }
    }
}

