package com.mobizonetech.aeon_wallet_cursor.data.featureflags

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class Feature { MOCK_INTERCEPTOR, AUTO_ADVANCE_SLIDES, ADVANCED_ANALYTICS }

interface FeatureFlags {
    fun isEnabled(feature: Feature): Flow<Boolean>
    suspend fun set(feature: Feature, enabled: Boolean)
}

@Singleton
class FeatureFlagsImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : FeatureFlags {

    override fun isEnabled(feature: Feature): Flow<Boolean> {
        val key = booleanPreferencesKey(feature.name)
        val default = when (feature) {
            Feature.MOCK_INTERCEPTOR -> true
            Feature.AUTO_ADVANCE_SLIDES -> false
            Feature.ADVANCED_ANALYTICS -> false
        }
        return dataStore.data.map { it[key] ?: default }
    }

    override suspend fun set(feature: Feature, enabled: Boolean) {
        val key = booleanPreferencesKey(feature.name)
        dataStore.edit { prefs -> prefs[key] = enabled }
    }
}


