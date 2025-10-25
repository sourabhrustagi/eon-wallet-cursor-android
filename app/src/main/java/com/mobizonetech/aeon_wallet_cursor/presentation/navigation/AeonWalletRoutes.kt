package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

object AeonWalletRoutes {
    // Auth Routes
    const val WELCOME = "welcome"
    const val ENTER_ID = "enterId"
    const val OTP = "otp"
    const val PASSCODE = "passcode"
    const val SECURITY_PASSCODE = "securityPasscode"
    
    // Main App Routes
    const val HOME = "home"
    const val LOANS = "loans"
    const val SCAN = "scan"
    const val CRYPTO = "crypto"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    
    // Detail Routes
    const val CARD_DETAIL = "cardDetail/{cardId}"
    const val LOAN_DETAIL = "loanDetail"
    
    // Application Routes
    const val BANK_PRODUCTS = "bankProducts"
    const val CARD_APPLICATION = "cardApplication/{cardId}"
    const val LOAN_APPLICATION = "loanApplication/{loanId}"
    
    // Payment Routes
    const val SEND_MONEY = "sendMoney"
    const val REQUEST_MONEY = "requestMoney"
    const val PAYMENT_SUCCESS = "paymentSuccess"
    const val UTILITY_PAYMENT = "utilityPayment/{billId}"
    const val CARD_REPAYMENT = "cardRepayment/{cardId}"
    const val LOAN_REPAYMENT = "loanRepayment/{loanId}"
    
    // Profile Routes
    const val FEEDBACK = "feedback"
    const val PRIVACY_POLICY = "privacyPolicy"
    const val TERMS_CONDITIONS = "termsConditions"
    const val REDEEM_POINTS = "redeemPoints"
    const val ORGANIZATIONS = "organizations"
    const val DONATION = "donation/{organizationId}"
    const val SETTINGS = "settings"
    const val CONTACT_US = "contactUs"
    
    // Common Routes
    const val WEBVIEW = "webview"
    const val CRYPTO_TRADING = "cryptoTrading"
    
    // Helper functions for navigation with parameters
    fun cardDetail(cardId: String) = "cardDetail/$cardId"
    fun cardApplication(cardId: String) = "cardApplication/$cardId"
    fun loanApplication(loanId: String) = "loanApplication/$loanId"
    fun utilityPayment(billId: String) = "utilityPayment/$billId"
    fun cardRepayment(cardId: String) = "cardRepayment/$cardId"
    fun loanRepayment(loanId: String) = "loanRepayment/$loanId"
    fun donation(organizationId: String) = "donation/$organizationId"
}
