package com.mobizonetech.aeon_wallet_cursor.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_CARD_NUMBER = stringPreferencesKey("user_card_number")
        private val USER_CARD_TYPE = stringPreferencesKey("user_card_type")
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }
    
    val userData: Flow<UserData> = context.dataStore.data.map { preferences ->
        UserData(
            id = preferences[USER_ID] ?: "",
            cardNumber = preferences[USER_CARD_NUMBER] ?: "",
            cardType = preferences[USER_CARD_TYPE] ?: "",
            passcode = "", // Don't store passcodes for security
            securityPasscode = "" // Don't store passcodes for security
        )
    }
    
    suspend fun setLoggedIn(userData: UserData) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userData.id
            preferences[USER_CARD_NUMBER] = userData.cardNumber
            preferences[USER_CARD_TYPE] = userData.cardType
        }
    }
    
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_ID] = ""
            preferences[USER_CARD_NUMBER] = ""
            preferences[USER_CARD_TYPE] = ""
        }
    }
}

data class UserData(
    val id: String = "",
    val cardNumber: String = "",
    val cardType: String = "",
    val passcode: String = "",
    val securityPasscode: String = ""
)
