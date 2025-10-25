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
        // Auth Screens with animations
        animatedComposable(AeonWalletDestinations.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(AeonWalletDestinations.EnterId.route) },
                onSignInClick = { navController.navigate(AeonWalletDestinations.EnterId.route) },
                onApplyForLoanCardClick = { 
                    navController.navigate(AeonWalletDestinations.CardApplication.createRoute("premium_card"))
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.EnterId.route) {
            EnterIdScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { id, cardNumber, cardType ->
                    navController.navigate(AeonWalletDestinations.Otp.route)
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.Otp.route) {
            OtpScreen(
                phoneNumber = "+1 234 567 8900",
                onBackClick = { navController.popBackStack() },
                onOtpVerified = { navController.navigate(AeonWalletDestinations.Passcode.route) }
            )
        }
        
        animatedComposable(AeonWalletDestinations.Passcode.route) {
            SetPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onPasscodeSet = { passcode ->
                    navController.navigate(AeonWalletDestinations.SecurityPasscode.route)
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.SecurityPasscode.route) {
            SetSecurityPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onSecurityPasscodeSet = { securityPasscode ->
                    navController.navigate(AeonWalletDestinations.Home.route)
                }
            )
        }

        // Main App Screens with tab navigation (no animation)
        tabComposable(AeonWalletDestinations.Home.route) {
            HomeScreen(
                userName = userName,
                onPromotionClick = { navController.navigate(AeonWalletDestinations.WebView.route) },
                onCardClick = { cardId ->
                    navController.navigate(AeonWalletDestinations.CardDetail.createRoute(cardId))
                },
                onBankProductsClick = { navController.navigate(AeonWalletDestinations.BankProducts.route) },
                onCardApplyClick = { cardId ->
                    navController.navigate(AeonWalletDestinations.CardApplication.createRoute(cardId))
                },
                onPointsClick = { navController.navigate(AeonWalletDestinations.RedeemPoints.route) },
                onUtilityBillClick = { billId ->
                    navController.navigate(AeonWalletDestinations.UtilityPayment.createRoute(billId))
                }
            )
        }
        
        tabComposable(AeonWalletDestinations.Loans.route) {
            LoansScreen(
                onLoanClick = { navController.navigate(AeonWalletDestinations.LoanDetail.route) },
                onBankProductsClick = { navController.navigate(AeonWalletDestinations.BankProducts.route) },
                onLoanApplyClick = { loanId ->
                    navController.navigate(AeonWalletDestinations.LoanApplication.createRoute(loanId))
                }
            )
        }
        
        tabComposable(AeonWalletDestinations.Scan.route) {
            ScanAndPayScreen(
                onSendMoneyClick = { navController.navigate(AeonWalletDestinations.SendMoney.route) },
                onRequestMoneyClick = { navController.navigate(AeonWalletDestinations.RequestMoney.route) }
            )
        }
        
        tabComposable(AeonWalletDestinations.Profile.route) {
            ProfileScreen(
                userName = userName,
                onLogout = onLogout,
                onFeedbackClick = { navController.navigate(AeonWalletDestinations.Feedback.route) },
                onPrivacyPolicyClick = { navController.navigate(AeonWalletDestinations.PrivacyPolicy.route) },
                onTermsClick = { navController.navigate(AeonWalletDestinations.TermsConditions.route) },
                onDonateClick = { navController.navigate(AeonWalletDestinations.Organizations.route) },
                onSettingsClick = { navController.navigate(AeonWalletDestinations.Settings.route) },
                onContactUsClick = { navController.navigate(AeonWalletDestinations.ContactUs.route) }
            )
        }
        
        tabComposable(AeonWalletDestinations.Crypto.route) {
            CryptoScreen(
                onTradingClick = { navController.navigate(AeonWalletDestinations.CryptoTrading.route) }
            )
        }
        
        tabComposable(AeonWalletDestinations.Notifications.route) {
            NotificationsScreen()
        }

        // Detail Screens with scale animation
        detailComposable(AeonWalletDestinations.LoanDetail.route) {
            LoanDetailScreen(
                onBackClick = { navController.popBackStack() },
                onRepaymentClick = { navController.navigate(AeonWalletDestinations.LoanRepayment.createRoute("loan_1")) }
            )
        }
        
        detailComposable(AeonWalletDestinations.CardDetail.route) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString(AeonWalletArgs.CARD_ID) ?: "card_1"
            CardDetailScreen(
                cardId = cardId,
                onBackClick = { navController.popBackStack() },
                onRepaymentClick = { navController.navigate(AeonWalletDestinations.CardRepayment.createRoute(cardId)) }
            )
        }

        // Application Screens with modal animation
        modalComposable(AeonWalletDestinations.BankProducts.route) {
            BankProductsScreen(
                onBackClick = { navController.popBackStack() },
                onApplyForCard = { card ->
                    navController.navigate(AeonWalletDestinations.CardApplication.createRoute(card.id))
                },
                onApplyForLoan = { loan ->
                    navController.navigate(AeonWalletDestinations.LoanApplication.createRoute(loan.id))
                }
            )
        }
        
        modalComposable(AeonWalletDestinations.CardApplication.route) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString(AeonWalletArgs.CARD_ID) ?: "premium_card"
            LoanCardApplicationScreen(
                onBackClick = { navController.popBackStack() },
                onApplicationSubmitted = { navController.popBackStack() }
            )
        }
        
        modalComposable(AeonWalletDestinations.LoanApplication.route) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString(AeonWalletArgs.LOAN_ID) ?: "personal_loan"
            LoanCardApplicationScreen(
                onBackClick = { navController.popBackStack() },
                onApplicationSubmitted = { navController.popBackStack() }
            )
        }

        // Payment Screens with modal animation
        modalComposable(AeonWalletDestinations.SendMoney.route) {
            SendMoneyScreen(
                onBackClick = { navController.popBackStack() },
                onSendClick = { recipient, amount, note ->
                    navController.navigate(AeonWalletDestinations.PaymentSuccess.route)
                }
            )
        }
        
        modalComposable(AeonWalletDestinations.RequestMoney.route) {
            RequestMoneyScreen(
                onBackClick = { navController.popBackStack() },
                onRequestClick = { requester, amount, note ->
                    navController.popBackStack()
                }
            )
        }
        
        modalComposable(AeonWalletDestinations.PaymentSuccess.route) {
            PaymentSuccessScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        modalComposable(AeonWalletDestinations.UtilityPayment.route) { backStackEntry ->
            val billId = backStackEntry.arguments?.getString(AeonWalletArgs.BILL_ID) ?: "unknown"
            UtilityPaymentScreen(
                billId = billId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }
        
        modalComposable(AeonWalletDestinations.LoanRepayment.route) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString(AeonWalletArgs.LOAN_ID) ?: "loan_1"
            LoanRepaymentScreen(
                loanId = loanId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }
        
        modalComposable(AeonWalletDestinations.CardRepayment.route) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString(AeonWalletArgs.CARD_ID) ?: "card_1"
            CardRepaymentScreen(
                cardId = cardId,
                onBackClick = { navController.popBackStack() },
                onPaymentComplete = { navController.popBackStack() }
            )
        }

        // Profile Screens with animations
        animatedComposable(AeonWalletDestinations.Feedback.route) {
            FeedbackScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitFeedback = { type, subject, message ->
                    navController.popBackStack()
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.PrivacyPolicy.route) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
        
        animatedComposable(AeonWalletDestinations.TermsConditions.route) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
        
        animatedComposable(AeonWalletDestinations.RedeemPoints.route) {
            RedeemPointsScreen(
                onBackClick = { navController.popBackStack() },
                onRedeemClick = { points, account ->
                    navController.popBackStack()
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.Organizations.route) {
            OrganizationsListScreen(
                onBackClick = { navController.popBackStack() },
                onOrganizationClick = { organizationId ->
                    navController.navigate(AeonWalletDestinations.Donation.createRoute(organizationId))
                }
            )
        }
        
        animatedComposable(AeonWalletDestinations.Donation.route) { backStackEntry ->
            val organizationId = backStackEntry.arguments?.getString(AeonWalletArgs.ORGANIZATION_ID) ?: "unknown"
            DonationScreen(
                organizationId = organizationId,
                onBackClick = { navController.popBackStack() },
                onDonationComplete = { navController.popBackStack() }
            )
        }
        
        animatedComposable(AeonWalletDestinations.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        animatedComposable(AeonWalletDestinations.ContactUs.route) {
            ContactUsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Common Screens
        animatedComposable(AeonWalletDestinations.WebView.route) {
            WebViewScreen(
                url = "https://www.google.com",
                onBackClick = { navController.popBackStack() }
            )
        }
        
        animatedComposable(AeonWalletDestinations.CryptoTrading.route) {
            CryptoTradingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
