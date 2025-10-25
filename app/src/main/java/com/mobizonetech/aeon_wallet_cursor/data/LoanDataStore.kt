package com.mobizonetech.aeon_wallet_cursor.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

private val Context.loanDataStore: DataStore<Preferences> by preferencesDataStore(name = "loan_data")

@Singleton
class LoanDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.loanDataStore

    companion object {
        private val LOANS_DATA_KEY = stringPreferencesKey("loans_data")
    }

    suspend fun saveLoans(loans: List<Loan>) {
        dataStore.edit { preferences ->
            val loansJson = loansToJson(loans)
            preferences[LOANS_DATA_KEY] = loansJson
        }
    }

    fun getLoans(): Flow<List<Loan>> {
        return dataStore.data.map { preferences ->
            val loansJson = preferences[LOANS_DATA_KEY] ?: getDefaultLoansJson()
            jsonToLoans(loansJson)
        }
    }

    private fun getDefaultLoansJson(): String {
        val defaultLoans = listOf(
            Loan(
                id = "loan_1",
                title = "Personal Loan",
                subtitle = "Outstanding",
                amount = "$15,000",
                backgroundColor = 0xFFE64A19,
                textColor = 0xFFFFFFFF,
                isLocked = true
            ),
            Loan(
                id = "loan_2",
                title = "Home Loan",
                subtitle = "Outstanding",
                amount = "$250,000",
                backgroundColor = 0xFF7B1FA2,
                textColor = 0xFFFFFFFF,
                isLocked = false
            ),
            Loan(
                id = "loan_3",
                title = "Car Loan",
                subtitle = "Outstanding",
                amount = "$35,000",
                backgroundColor = 0xFF1976D2,
                textColor = 0xFFFFFFFF,
                isLocked = true
            )
        )
        return loansToJson(defaultLoans)
    }

    private fun loansToJson(loans: List<Loan>): String {
        // Simple JSON serialization - in production, use proper JSON library like Gson or Moshi
        return loans.joinToString("|") { loan ->
            "${loan.id},${loan.title},${loan.subtitle},${loan.amount}," +
            "${loan.backgroundColor},${loan.textColor},${loan.isLocked}"
        }
    }

    private fun jsonToLoans(json: String): List<Loan> {
        if (json.isEmpty()) return emptyList()
        
        return json.split("|").mapNotNull { loanString ->
            try {
                val parts = loanString.split(",")
                if (parts.size >= 7) {
                    Loan(
                        id = parts[0],
                        title = parts[1],
                        subtitle = parts[2],
                        amount = parts[3],
                        backgroundColor = parts[4].toLong(),
                        textColor = parts[5].toLong(),
                        isLocked = parts[6].toBoolean()
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}
