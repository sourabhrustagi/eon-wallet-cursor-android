package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Navigation animations and transitions for AEON Wallet
 */
object AeonWalletAnimations {
    
    // Slide animations
    val slideInFromRight = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = spring())
    
    val slideOutToRight = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeOut(animationSpec = spring())
    
    val slideInFromLeft = slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = spring())
    
    val slideOutToLeft = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeOut(animationSpec = spring())
    
    // Vertical slide animations
    val slideInFromBottom = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = spring())
    
    val slideOutToBottom = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeOut(animationSpec = spring())
    
    // Scale animations
    val scaleIn = scaleIn(
        initialScale = 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = spring())
    
    val scaleOut = scaleOut(
        targetScale = 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeOut(animationSpec = spring())
    
    // No animation for same-level navigation
    val noAnimation = EnterTransition.None + ExitTransition.None
}

/**
 * Extension function for composable with custom animations
 */
fun NavGraphBuilder.animatedComposable(
    route: String,
    enterTransition: EnterTransition = AeonWalletAnimations.slideInFromRight,
    exitTransition: ExitTransition = AeonWalletAnimations.slideOutToLeft,
    popEnterTransition: EnterTransition = AeonWalletAnimations.slideInFromLeft,
    popExitTransition: ExitTransition = AeonWalletAnimations.slideOutToRight,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition },
        content = content
    )
}

/**
 * Extension function for modal-style composables (slide from bottom)
 */
fun NavGraphBuilder.modalComposable(
    route: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { AeonWalletAnimations.slideInFromBottom },
        exitTransition = { AeonWalletAnimations.slideOutToBottom },
        popEnterTransition = { AeonWalletAnimations.slideInFromBottom },
        popExitTransition = { AeonWalletAnimations.slideOutToBottom },
        content = content
    )
}

/**
 * Extension function for detail screens (scale animation)
 */
fun NavGraphBuilder.detailComposable(
    route: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { AeonWalletAnimations.scaleIn },
        exitTransition = { AeonWalletAnimations.scaleOut },
        popEnterTransition = { AeonWalletAnimations.slideInFromLeft },
        popExitTransition = { AeonWalletAnimations.slideOutToRight },
        content = content
    )
}

/**
 * Extension function for same-level navigation (no animation)
 */
fun NavGraphBuilder.tabComposable(
    route: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { AeonWalletAnimations.noAnimation },
        exitTransition = { AeonWalletAnimations.noAnimation },
        popEnterTransition = { AeonWalletAnimations.noAnimation },
        popExitTransition = { AeonWalletAnimations.noAnimation },
        content = content
    )
}
