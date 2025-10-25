package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.model.CardBrand
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import kotlinx.coroutines.flow.Flow

class CardRepositoryImpl(
    private val unlockPreferences: UnlockPreferences
) : CardRepository {
    
    override suspend fun getCards(): List<Card> {
        return listOf(
            Card(
                id = "card_1",
                title = "Credit Card",
                subtitle = "Available Balance",
                amount = "$5,000",
                cardNumber = "4532123456789012",
                maskedCardNumber = "4532 **** **** 9012",
                cardHolderName = "JOHN DOE",
                expiryDate = "12/25",
                cvv = "123",
                cardBrand = CardBrand.VISA,
                backgroundColor = 0xFF1A1A2E,
                textColor = 0xFFFFFFFF,
                isLocked = true
            ),
            Card(
                id = "card_2",
                title = "Debit Card",
                subtitle = "Available Balance",
                amount = "$2,500",
                cardNumber = "5555123456789012",
                maskedCardNumber = "5555 **** **** 9012",
                cardHolderName = "JOHN DOE",
                expiryDate = "10/26",
                cvv = "456",
                cardBrand = CardBrand.MASTERCARD,
                backgroundColor = 0xFF0F3460,
                textColor = 0xFFFFFFFF,
                isLocked = false
            ),
            Card(
                id = "card_3",
                title = "Premium Card",
                subtitle = "Available Balance",
                amount = "$10,000",
                cardNumber = "3782123456789012",
                maskedCardNumber = "3782 **** **** 9012",
                cardHolderName = "JOHN DOE",
                expiryDate = "08/27",
                cvv = "789",
                cardBrand = CardBrand.AMEX,
                backgroundColor = 0xFF2E7D32,
                textColor = 0xFFFFFFFF,
                isLocked = true
            )
        )
    }
    
    override suspend fun getCardById(id: String): Card? {
        return getCards().find { it.id == id }
    }
    
    override suspend fun unlockCard(cardId: String) {
        unlockPreferences.unlockCard(cardId)
    }
    
    override suspend fun lockCard(cardId: String) {
        unlockPreferences.lockCard(cardId)
    }
    
    override fun getUnlockedCards(): Flow<Set<String>> {
        return unlockPreferences.unlockedCards
    }
}
