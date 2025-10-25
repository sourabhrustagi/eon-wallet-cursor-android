package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.navOptions

/**
 * Deep linking configuration for AEON Wallet
 */
object AeonWalletDeepLinks {
    
    // Base deep link URI
    private const val BASE_URI = "aeonwallet://"
    
    // Deep link patterns
    const val CARD_DETAIL_DEEP_LINK = "$BASE_URI/card/{cardId}"
    const val LOAN_DETAIL_DEEP_LINK = "$BASE_URI/loan/{loanId}"
    const val PAYMENT_DEEP_LINK = "$BASE_URI/payment/{paymentId}"
    const val NOTIFICATION_DEEP_LINK = "$BASE_URI/notification/{notificationId}"
    const val CRYPTO_DEEP_LINK = "$BASE_URI/crypto/{cryptoId}"
    
    /**
     * Creates a deep link request for card details
     */
    fun createCardDetailDeepLink(cardId: String): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(Uri.parse("$BASE_URI/card/$cardId"))
            .build()
    }
    
    /**
     * Creates a deep link request for loan details
     */
    fun createLoanDetailDeepLink(loanId: String): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(Uri.parse("$BASE_URI/loan/$loanId"))
            .build()
    }
    
    /**
     * Creates a deep link request for payment details
     */
    fun createPaymentDeepLink(paymentId: String): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(Uri.parse("$BASE_URI/payment/$paymentId"))
            .build()
    }
    
    /**
     * Creates a deep link request for notifications
     */
    fun createNotificationDeepLink(notificationId: String): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(Uri.parse("$BASE_URI/notification/$notificationId"))
            .build()
    }
    
    /**
     * Creates a deep link request for crypto trading
     */
    fun createCryptoDeepLink(cryptoId: String): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(Uri.parse("$BASE_URI/crypto/$cryptoId"))
            .build()
    }
    
    /**
     * Extension function to navigate with deep link
     */
    fun NavController.navigateWithDeepLink(
        deepLinkRequest: NavDeepLinkRequest,
        navOptions: NavOptions? = null
    ) {
        navigate(deepLinkRequest, navOptions)
    }
    
    /**
     * Extension function to navigate to card detail with deep link
     */
    fun NavController.navigateToCardDetail(cardId: String) {
        val deepLinkRequest = createCardDetailDeepLink(cardId)
        val navOptions = navOptions {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigateWithDeepLink(deepLinkRequest, navOptions)
    }
    
    /**
     * Extension function to navigate to loan detail with deep link
     */
    fun NavController.navigateToLoanDetail(loanId: String) {
        val deepLinkRequest = createLoanDetailDeepLink(loanId)
        val navOptions = navOptions {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigateWithDeepLink(deepLinkRequest, navOptions)
    }
    
    /**
     * Extension function to navigate to payment with deep link
     */
    fun NavController.navigateToPayment(paymentId: String) {
        val deepLinkRequest = createPaymentDeepLink(paymentId)
        val navOptions = navOptions {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigateWithDeepLink(deepLinkRequest, navOptions)
    }
    
    /**
     * Extension function to navigate to notification with deep link
     */
    fun NavController.navigateToNotification(notificationId: String) {
        val deepLinkRequest = createNotificationDeepLink(notificationId)
        val navOptions = navOptions {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigateWithDeepLink(deepLinkRequest, navOptions)
    }
    
    /**
     * Extension function to navigate to crypto with deep link
     */
    fun NavController.navigateToCrypto(cryptoId: String) {
        val deepLinkRequest = createCryptoDeepLink(cryptoId)
        val navOptions = navOptions {
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        navigateWithDeepLink(deepLinkRequest, navOptions)
    }
}

/**
 * Deep link handler for processing incoming deep links
 */
class AeonWalletDeepLinkHandler {
    
    /**
     * Processes a deep link URI and returns the appropriate navigation action
     */
    fun processDeepLink(uri: Uri): DeepLinkAction? {
        return when {
            uri.toString().startsWith("aeonwallet://card/") -> {
                val cardId = uri.lastPathSegment
                DeepLinkAction.NavigateToCardDetail(cardId ?: "")
            }
            uri.toString().startsWith("aeonwallet://loan/") -> {
                val loanId = uri.lastPathSegment
                DeepLinkAction.NavigateToLoanDetail(loanId ?: "")
            }
            uri.toString().startsWith("aeonwallet://payment/") -> {
                val paymentId = uri.lastPathSegment
                DeepLinkAction.NavigateToPayment(paymentId ?: "")
            }
            uri.toString().startsWith("aeonwallet://notification/") -> {
                val notificationId = uri.lastPathSegment
                DeepLinkAction.NavigateToNotification(notificationId ?: "")
            }
            uri.toString().startsWith("aeonwallet://crypto/") -> {
                val cryptoId = uri.lastPathSegment
                DeepLinkAction.NavigateToCrypto(cryptoId ?: "")
            }
            else -> null
        }
    }
}

/**
 * Sealed class representing deep link actions
 */
sealed class DeepLinkAction {
    data class NavigateToCardDetail(val cardId: String) : DeepLinkAction()
    data class NavigateToLoanDetail(val loanId: String) : DeepLinkAction()
    data class NavigateToPayment(val paymentId: String) : DeepLinkAction()
    data class NavigateToNotification(val notificationId: String) : DeepLinkAction()
    data class NavigateToCrypto(val cryptoId: String) : DeepLinkAction()
}
