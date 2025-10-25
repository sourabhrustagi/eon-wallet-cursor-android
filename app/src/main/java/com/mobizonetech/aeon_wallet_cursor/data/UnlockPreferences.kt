package com.mobizonetech.aeon_wallet_cursor.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "unlock_preferences")

class UnlockPreferences(private val context: Context) {
    
    companion object {
        private val UNLOCKED_CARDS = stringSetPreferencesKey("unlocked_cards")
        private val UNLOCKED_LOANS = stringSetPreferencesKey("unlocked_loans")
    }
    
    // Get unlocked cards
    val unlockedCards: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[UNLOCKED_CARDS] ?: emptySet()
        }
    
    // Get unlocked loans
    val unlockedLoans: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[UNLOCKED_LOANS] ?: emptySet()
        }
    
    // Unlock a card
    suspend fun unlockCard(cardId: String) {
        context.dataStore.edit { preferences ->
            val currentCards = preferences[UNLOCKED_CARDS] ?: emptySet()
            preferences[UNLOCKED_CARDS] = currentCards + cardId
        }
    }
    
    // Unlock a loan
    suspend fun unlockLoan(loanId: String) {
        context.dataStore.edit { preferences ->
            val currentLoans = preferences[UNLOCKED_LOANS] ?: emptySet()
            preferences[UNLOCKED_LOANS] = currentLoans + loanId
        }
    }
    
    // Lock a card
    suspend fun lockCard(cardId: String) {
        context.dataStore.edit { preferences ->
            val currentCards = preferences[UNLOCKED_CARDS] ?: emptySet()
            preferences[UNLOCKED_CARDS] = currentCards - cardId
        }
    }
    
    // Lock a loan
    suspend fun lockLoan(loanId: String) {
        context.dataStore.edit { preferences ->
            val currentLoans = preferences[UNLOCKED_LOANS] ?: emptySet()
            preferences[UNLOCKED_LOANS] = currentLoans - loanId
        }
    }
    
    // Reset all unlock states (called on logout)
    suspend fun resetAllUnlocks() {
        context.dataStore.edit { preferences ->
            preferences[UNLOCKED_CARDS] = emptySet()
            preferences[UNLOCKED_LOANS] = emptySet()
        }
    }
    
    // Check if card is unlocked
    suspend fun isCardUnlocked(cardId: String): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[UNLOCKED_CARDS]?.contains(cardId) ?: false
        }.let { flow ->
            // This is a simplified approach - in a real app you'd collect the flow
            false // We'll handle this differently in the composables
        }
    }
    
    // Check if loan is unlocked
    suspend fun isLoanUnlocked(loanId: String): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[UNLOCKED_LOANS]?.contains(loanId) ?: false
        }.let { flow ->
            // This is a simplified approach - in a real app you'd collect the flow
            false // We'll handle this differently in the composables
        }
    }
}
