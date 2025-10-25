package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.data.CardDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val unlockPreferences: UnlockPreferences,
    private val cardDataStore: CardDataStore
) : CardRepository {
    
    override suspend fun getCards(): List<Card> {
        return cardDataStore.getCards().first()
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
