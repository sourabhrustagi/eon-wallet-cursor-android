package com.mobizonetech.aeon_wallet_cursor.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.model.CardBrand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.cardDataStore: DataStore<Preferences> by preferencesDataStore(name = "card_data")

@Singleton
class CardDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.cardDataStore

    companion object {
        private val CARDS_DATA_KEY = stringPreferencesKey("cards_data")
    }

    suspend fun saveCards(cards: List<Card>) {
        dataStore.edit { preferences ->
            val cardsJson = cardsToJson(cards)
            preferences[CARDS_DATA_KEY] = cardsJson
        }
    }

    fun getCards(): Flow<List<Card>> {
        return dataStore.data.map { preferences ->
            val cardsJson = preferences[CARDS_DATA_KEY] ?: getDefaultCardsJson()
            jsonToCards(cardsJson)
        }
    }

    private fun getDefaultCardsJson(): String {
        val defaultCards = listOf(
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
        return cardsToJson(defaultCards)
    }

    private fun cardsToJson(cards: List<Card>): String {
        // Simple JSON serialization - in production, use proper JSON library like Gson or Moshi
        return cards.joinToString("|") { card ->
            "${card.id},${card.title},${card.subtitle},${card.amount},${card.cardNumber}," +
            "${card.maskedCardNumber},${card.cardHolderName},${card.expiryDate},${card.cvv}," +
            "${card.cardBrand.name},${card.backgroundColor},${card.textColor},${card.isLocked}"
        }
    }

    private fun jsonToCards(json: String): List<Card> {
        if (json.isEmpty()) return emptyList()
        
        return json.split("|").mapNotNull { cardString ->
            try {
                val parts = cardString.split(",")
                if (parts.size >= 13) {
                    Card(
                        id = parts[0],
                        title = parts[1],
                        subtitle = parts[2],
                        amount = parts[3],
                        cardNumber = parts[4],
                        maskedCardNumber = parts[5],
                        cardHolderName = parts[6],
                        expiryDate = parts[7],
                        cvv = parts[8],
                        cardBrand = CardBrand.valueOf(parts[9]),
                        backgroundColor = parts[10].toLong(),
                        textColor = parts[11].toLong(),
                        isLocked = parts[12].toBoolean()
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}
