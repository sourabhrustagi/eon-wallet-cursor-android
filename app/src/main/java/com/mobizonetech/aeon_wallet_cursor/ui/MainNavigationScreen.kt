package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen(
    userName: String = "John Doe",
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    var currentTab by remember { mutableStateOf("home") }
    var currentRoute by remember { mutableStateOf("home") }
    val context = LocalContext.current
    val unlockPreferences = remember { UnlockPreferences(context) }
    val unlockedCards by unlockPreferences.unlockedCards.collectAsState(initial = emptySet())
    
    Scaffold(
                bottomBar = {
                    if (currentRoute != "webview" && currentRoute != "loanDetail" && currentRoute != "cardDetail" && 
                        currentRoute != "bankProducts" && currentRoute != "cardApplication" && currentRoute != "loanApplication" &&
                        currentRoute != "sendMoney" && currentRoute != "requestMoney" && currentRoute != "feedback" &&
                        currentRoute != "privacyPolicy" && currentRoute != "termsConditions" && currentRoute != "redeemPoints" &&
                        currentRoute != "organizations" && currentRoute != "donation" && currentRoute != "cardRepayment" && 
                        currentRoute != "loanRepayment" && currentRoute != "cryptoTrading" && currentRoute != "settings" && 
                        currentRoute != "contactUs") {
                MainBottomNavigationBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        when (tab) {
                            "home" -> navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            "loans" -> navController.navigate("loans") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            "scan" -> navController.navigate("scan") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            "crypto" -> navController.navigate("crypto") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            "notifications" -> navController.navigate("notifications") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            "profile" -> navController.navigate("profile") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                currentRoute = "home"
                HomeScreen(
                    userName = userName,
                    onPromotionClick = {
                        navController.navigate("webview")
                    },
                    onCardClick = { cardId ->
                        // Always navigate to cardDetail, but pass cardId as parameter
                        navController.navigate("cardDetail/$cardId")
                    },
                    onBankProductsClick = {
                        navController.navigate("bankProducts")
                    },
                    onCardApplyClick = { cardId ->
                        navController.navigate("cardApplication/$cardId")
                    },
                    onPointsClick = {
                        navController.navigate("redeemPoints")
                    },
                    onUtilityBillClick = { billId ->
                        // Handle utility bill click - could navigate to bill payment screen
                        // For now, just show a placeholder
                    }
                )
            }
            composable("loans") {
                currentRoute = "loans"
                LoansScreen(
                    onLoanClick = {
                        navController.navigate("loanDetail")
                    },
                    onBankProductsClick = {
                        navController.navigate("bankProducts")
                    },
                    onLoanApplyClick = { loanId ->
                        navController.navigate("loanApplication/$loanId")
                    }
                )
            }
            composable("scan") {
                currentRoute = "scan"
                ScanAndPayScreen(
                    onSendMoneyClick = {
                        navController.navigate("sendMoney")
                    },
                    onRequestMoneyClick = {
                        navController.navigate("requestMoney")
                    }
                )
            }
            composable("profile") {
                currentRoute = "profile"
                ProfileScreen(
                    userName = userName,
                    onLogout = onLogout,
                    onFeedbackClick = {
                        navController.navigate("feedback")
                    },
                    onPrivacyPolicyClick = {
                        navController.navigate("privacyPolicy")
                    },
                    onTermsClick = {
                        navController.navigate("termsConditions")
                    },
                    onDonateClick = {
                        navController.navigate("organizations")
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onContactUsClick = {
                        navController.navigate("contactUs")
                    }
                )
            }
            composable("crypto") {
                currentRoute = "crypto"
                CryptoScreen(
                    onTradingClick = {
                        navController.navigate("cryptoTrading")
                    }
                )
            }
            composable("cryptoTrading") {
                currentRoute = "cryptoTrading"
                CryptoTradingScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("notifications") {
                currentRoute = "notifications"
                NotificationsScreen()
            }
            composable("loanDetail") {
                currentRoute = "loanDetail"
                LoanDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRepaymentClick = {
                        navController.navigate("loanRepayment/loan_1")
                    }
                )
            }
            composable("cardDetail/{cardId}") { backStackEntry ->
                currentRoute = "cardDetail"
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "card_1"
                CardDetailScreen(
                    cardId = cardId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRepaymentClick = {
                        navController.navigate("cardRepayment/$cardId")
                    }
                )
            }
            composable("webview") {
                currentRoute = "webview"
                WebViewScreen(
                    url = "https://www.google.com",
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("bankProducts") {
                currentRoute = "bankProducts"
                BankProductsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onApplyForCard = { card ->
                        navController.navigate("cardApplication/${card.id}")
                    },
                    onApplyForLoan = { loan ->
                        navController.navigate("loanApplication/${loan.id}")
                    }
                )
            }
            composable("cardApplication/{cardId}") { backStackEntry ->
                currentRoute = "cardApplication"
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "premium_card"
                LoanCardApplicationScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onApplicationSubmitted = {
                        navController.popBackStack()
                    }
                )
            }
            composable("loanApplication/{loanId}") { backStackEntry ->
                currentRoute = "loanApplication"
                val loanId = backStackEntry.arguments?.getString("loanId") ?: "personal_loan"
                LoanCardApplicationScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onApplicationSubmitted = {
                        navController.popBackStack()
                    }
                )
            }
            composable("sendMoney") {
                currentRoute = "sendMoney"
                SendMoneyScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSendClick = { recipient, amount, note ->
                        // Handle send money logic here
                        navController.popBackStack()
                    }
                )
            }
            composable("requestMoney") {
                currentRoute = "requestMoney"
                RequestMoneyScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRequestClick = { requester, amount, note ->
                        // Handle request money logic here
                        navController.popBackStack()
                    }
                )
            }
            composable("feedback") {
                currentRoute = "feedback"
                FeedbackScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSubmitFeedback = { type, subject, message ->
                        // Handle feedback submission here
                        navController.popBackStack()
                    }
                )
            }
            composable("privacyPolicy") {
                currentRoute = "privacyPolicy"
                WebViewScreen(
                    url = "https://www.google.com",
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("termsConditions") {
                currentRoute = "termsConditions"
                WebViewScreen(
                    url = "https://www.google.com",
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("redeemPoints") {
                currentRoute = "redeemPoints"
                RedeemPointsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRedeemClick = { points, account ->
                        // Handle points redemption logic here
                        navController.popBackStack()
                    }
                )
            }
            composable("organizations") {
                currentRoute = "organizations"
                OrganizationsListScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onOrganizationClick = { organizationId ->
                        navController.navigate("donation/$organizationId")
                    }
                )
            }
            composable("settings") {
                currentRoute = "settings"
                SettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("contactUs") {
                currentRoute = "contactUs"
                ContactUsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("cardRepayment/{cardId}") { backStackEntry ->
                currentRoute = "cardRepayment"
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "card_1"
                CardRepaymentScreen(
                    cardId = cardId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentComplete = {
                        navController.popBackStack()
                    }
                )
            }
            composable("loanRepayment/{loanId}") { backStackEntry ->
                currentRoute = "loanRepayment"
                val loanId = backStackEntry.arguments?.getString("loanId") ?: "loan_1"
                LoanRepaymentScreen(
                    loanId = loanId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentComplete = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf(
        Triple("home", "Cards", Icons.Default.Home),
        Triple("loans", "Loans", Icons.Default.Home),
        Triple("scan", "Scan & Pay", Icons.Default.Add),
        Triple("crypto", "Crypto", Icons.Default.Star),
        Triple("notifications", "Notifications", Icons.Default.Notifications),
        Triple("profile", "Profile", Icons.Default.Person)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (route, label, icon) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(route) }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (currentTab == route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (currentTab == route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
