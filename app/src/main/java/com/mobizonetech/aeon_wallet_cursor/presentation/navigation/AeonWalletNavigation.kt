package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.EnterIdScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.OtpScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.SetPasscodeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.SetSecurityPasscodeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth.WelcomeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.cards.BankProductsScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.cards.CardDetailScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.cards.CardRepaymentScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.crypto.CryptoScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.crypto.CryptoTradingScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.home.HomeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.home.ScanAndPayScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.loans.LoanCardApplicationScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.loans.LoanDetailScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.loans.LoanRepaymentScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.loans.LoansScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.notifications.NotificationsScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.payments.PaymentSuccessScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.payments.RequestMoneyScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.payments.SendMoneyScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.payments.UtilityPaymentScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.ContactUsScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.DonationScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.FeedbackScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.OrganizationsListScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.ProfileScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.RedeemPointsScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.profile.SettingsScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.common.WebViewScreen

@Composable
fun AeonWalletNavigation(
    navController: NavHostController = rememberNavController(),
    userName: String,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AeonWalletDestinations.Home.route
    ) {
        // Auth Screens
        composable(AeonWalletRoutes.WELCOME) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(AeonWalletRoutes.ENTER_ID) },
                onSignInClick = { navController.navigate(AeonWalletRoutes.ENTER_ID) },
                onApplyForLoanCardClick = { navController.navigate(AeonWalletRoutes.CARD_APPLICATION.replace("{cardId}", "premium_card")) }
            )
        }
        
        composable(AeonWalletRoutes.ENTER_ID) {
            EnterIdScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { id, cardNumber, cardType ->
                    navController.navigate(AeonWalletRoutes.OTP)
                }
            )
        }
        
        composable(AeonWalletRoutes.OTP) {
            OtpScreen(
                phoneNumber = "+1 234 567 8900",
                onBackClick = { navController.popBackStack() },
                onOtpVerified = { navController.navigate(AeonWalletRoutes.PASSCODE) }
            )
        }
        
        composable(AeonWalletRoutes.PASSCODE) {
            SetPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onPasscodeSet = { passcode ->
                    navController.navigate(AeonWalletRoutes.SECURITY_PASSCODE)
                }
            )
        }
        
        composable(AeonWalletRoutes.SECURITY_PASSCODE) {
            SetSecurityPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onSecurityPasscodeSet = { securityPasscode ->
                    navController.navigate(AeonWalletRoutes.HOME)
                }
            )
        }

        // Main App Screens
        composable(AeonWalletRoutes.HOME) {
            HomeScreen(
                userName = userName,
                onPromotionClick = { navController.navigate(AeonWalletRoutes.WEBVIEW) },
                onCardClick = { cardId ->
                    navController.navigate(AeonWalletRoutes.cardDetail(cardId))
                },
                onBankProductsClick = { navController.navigate(AeonWalletRoutes.BANK_PRODUCTS) },
                onCardApplyClick = { cardId ->
                    navController.navigate(AeonWalletRoutes.cardApplication(cardId))
                },
                onPointsClick = { navController.navigate(AeonWalletRoutes.REDEEM_POINTS) },
                onUtilityBillClick = { billId ->
                    navController.navigate(AeonWalletRoutes.utilityPayment(billId))
                }
            )
        }
        
        composable(AeonWalletRoutes.LOANS) {
            LoansScreen(
                onLoanClick = { navController.navigate(AeonWalletRoutes.LOAN_DETAIL) },
                onBankProductsClick = { navController.navigate(AeonWalletRoutes.BANK_PRODUCTS) },
                onLoanApplyClick = { loanId ->
                    navController.navigate(AeonWalletRoutes.loanApplication(loanId))
                }
            )
        }
        
        composable(AeonWalletRoutes.SCAN) {
            ScanAndPayScreen(
                onSendMoneyClick = { navController.navigate(AeonWalletRoutes.SEND_MONEY) },
                onRequestMoneyClick = { navController.navigate(AeonWalletRoutes.REQUEST_MONEY) }
            )
        }
        
        composable(AeonWalletRoutes.PROFILE) {
            ProfileScreen(
                userName = userName,
                onLogout = onLogout,
                onFeedbackClick = { navController.navigate(AeonWalletRoutes.FEEDBACK) },
                onPrivacyPolicyClick = { navController.navigate(AeonWalletRoutes.PRIVACY_POLICY) },
                onTermsClick = { navController.navigate(AeonWalletRoutes.TERMS_CONDITIONS) },
                onDonateClick = { navController.navigate(AeonWalletRoutes.ORGANIZATIONS) },
                onSettingsClick = { navController.navigate(AeonWalletRoutes.SETTINGS) },
                onContactUsClick = { navController.navigate(AeonWalletRoutes.CONTACT_US) }
            )
        }
        
        composable(AeonWalletRoutes.CRYPTO) {
            CryptoScreen(
                onTradingClick = { navController.navigate(AeonWalletRoutes.CRYPTO_TRADING) }
            )
        }
        
        composable(AeonWalletRoutes.CRYPTO_TRADING) {
            CryptoTradingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.NOTIFICATIONS) {
            NotificationsScreen()
        }

        // Detail Screens
        composable(AeonWalletRoutes.LOAN_DETAIL) {
            LoanDetailScreen(
                onBackClick = { navController.popBackStack() },
                onRepaymentClick = { navController.navigate(AeonWalletRoutes.loanRepayment("loan_1")) }
            )
        }
        
        composable(AeonWalletRoutes.CARD_DETAIL) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: "card_1"
            CardDetailScreen(
                cardId = cardId,
                onBackClick = { navController.popBackStack() },
                onRepaymentClick = { navController.navigate(AeonWalletRoutes.cardRepayment(cardId)) }
            )
        }

        // Application Screens
        composable(AeonWalletRoutes.BANK_PRODUCTS) {
            BankProductsScreen(
                onBackClick = { navController.popBackStack() },
                onApplyForCard = { card ->
                    navController.navigate(AeonWalletRoutes.cardApplication(card.id))
                },
                onApplyForLoan = { loan ->
                    navController.navigate(AeonWalletRoutes.loanApplication(loan.id))
                }
            )
        }
        
        composable(AeonWalletRoutes.CARD_APPLICATION) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: "premium_card"
            LoanCardApplicationScreen(
                onBackClick = { navController.popBackStack() },
                onApplicationSubmitted = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.LOAN_APPLICATION) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: "personal_loan"
            LoanCardApplicationScreen(
                onBackClick = { navController.popBackStack() },
                onApplicationSubmitted = { navController.popBackStack() }
            )
        }

        // Payment Screens
        composable(AeonWalletRoutes.SEND_MONEY) {
            SendMoneyScreen(
                onBackClick = { navController.popBackStack() },
                onSendClick = { recipient, amount, note ->
                    navController.navigate(AeonWalletRoutes.PAYMENT_SUCCESS)
                }
            )
        }
        
        composable(AeonWalletRoutes.REQUEST_MONEY) {
            RequestMoneyScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requester, amount, note ->
                    navController.popBackStack()
                }
            )
        }
        
        composable(AeonWalletRoutes.PAYMENT_SUCCESS) {
            PaymentSuccessScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.UTILITY_PAYMENT) { backStackEntry ->
            val billId = backStackEntry.arguments?.getString("billId") ?: "unknown"
            UtilityPaymentScreen(
                billId = billId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.LOAN_REPAYMENT) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: "loan_1"
            LoanRepaymentScreen(
                loanId = loanId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.CARD_REPAYMENT) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: "card_1"
            CardRepaymentScreen(
                cardId = cardId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }

        // Profile Screens
        composable(AeonWalletRoutes.FEEDBACK) {
            FeedbackScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitFeedback = { type, subject, message ->
                    navController.popBackStack()
                }
            )
        }
        
        composable(AeonWalletRoutes.PRIVACY_POLICY) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.TERMS_CONDITIONS) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.REDEEM_POINTS) {
            RedeemPointsScreen(
                onBackClick = { navController.popBackStack() },
                onRedeemClick = { points, account ->
                    navController.popBackStack()
                }
            )
        }
        
        composable(AeonWalletRoutes.ORGANIZATIONS) {
            OrganizationsListScreen(
                onBackClick = { navController.popBackStack() },
                onOrganizationClick = { organizationId ->
                    navController.navigate(AeonWalletRoutes.donation(organizationId))
                }
            )
        }
        
        composable(AeonWalletRoutes.DONATION) { backStackEntry ->
            val organizationId = backStackEntry.arguments?.getString("organizationId") ?: "unknown"
            DonationScreen(
                organizationId = organizationId,
                onBackClick = { navController.popBackStack() },
                onDonationComplete = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.SETTINGS) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(AeonWalletRoutes.CONTACT_US) {
            ContactUsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Common Screens
        composable(AeonWalletRoutes.WEBVIEW) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
