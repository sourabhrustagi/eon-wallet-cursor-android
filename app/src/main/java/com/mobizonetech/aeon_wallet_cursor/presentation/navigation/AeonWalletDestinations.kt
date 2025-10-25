package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Type-safe navigation destinations for AEON Wallet
 */
sealed class AeonWalletDestinations(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    // Auth Destinations
    object Welcome : AeonWalletDestinations("welcome")
    object EnterId : AeonWalletDestinations("enterId")
    object Otp : AeonWalletDestinations("otp")
    object Passcode : AeonWalletDestinations("passcode")
    object SecurityPasscode : AeonWalletDestinations("securityPasscode")
    
    // Main App Destinations
    object Home : AeonWalletDestinations("home")
    object Loans : AeonWalletDestinations("loans")
    object Scan : AeonWalletDestinations("scan")
    object Crypto : AeonWalletDestinations("crypto")
    object Notifications : AeonWalletDestinations("notifications")
    object Profile : AeonWalletDestinations("profile")
    
    // Detail Destinations with Arguments
    object CardDetail : AeonWalletDestinations(
        route = "cardDetail/{cardId}",
        arguments = listOf(
            navArgument("cardId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(cardId: String) = "cardDetail/$cardId"
    }
    
    object LoanDetail : AeonWalletDestinations("loanDetail")
    
    // Application Destinations
    object BankProducts : AeonWalletDestinations("bankProducts")
    
    object CardApplication : AeonWalletDestinations(
        route = "cardApplication/{cardId}",
        arguments = listOf(
            navArgument("cardId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(cardId: String) = "cardApplication/$cardId"
    }
    
    object LoanApplication : AeonWalletDestinations(
        route = "loanApplication/{loanId}",
        arguments = listOf(
            navArgument("loanId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(loanId: String) = "loanApplication/$loanId"
    }
    
    // Payment Destinations
    object SendMoney : AeonWalletDestinations("sendMoney")
    object RequestMoney : AeonWalletDestinations("requestMoney")
    object PaymentSuccess : AeonWalletDestinations("paymentSuccess")
    
    object UtilityPayment : AeonWalletDestinations(
        route = "utilityPayment/{billId}",
        arguments = listOf(
            navArgument("billId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(billId: String) = "utilityPayment/$billId"
    }
    
    object CardRepayment : AeonWalletDestinations(
        route = "cardRepayment/{cardId}",
        arguments = listOf(
            navArgument("cardId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(cardId: String) = "cardRepayment/$cardId"
    }
    
    object LoanRepayment : AeonWalletDestinations(
        route = "loanRepayment/{loanId}",
        arguments = listOf(
            navArgument("loanId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(loanId: String) = "loanRepayment/$loanId"
    }
    
    // Profile Destinations
    object Feedback : AeonWalletDestinations("feedback")
    object PrivacyPolicy : AeonWalletDestinations("privacyPolicy")
    object TermsConditions : AeonWalletDestinations("termsConditions")
    object RedeemPoints : AeonWalletDestinations("redeemPoints")
    object Organizations : AeonWalletDestinations("organizations")
    
    object Donation : AeonWalletDestinations(
        route = "donation/{organizationId}",
        arguments = listOf(
            navArgument("organizationId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(organizationId: String) = "donation/$organizationId"
    }
    
    object Settings : AeonWalletDestinations("settings")
    object ContactUs : AeonWalletDestinations("contactUs")
    
    // Common Destinations
    object WebView : AeonWalletDestinations("webview")
    object CryptoTrading : AeonWalletDestinations("cryptoTrading")
}

/**
 * Navigation arguments for type-safe argument extraction
 */
object AeonWalletArgs {
    const val CARD_ID = "cardId"
    const val LOAN_ID = "loanId"
    const val BILL_ID = "billId"
    const val ORGANIZATION_ID = "organizationId"
}
