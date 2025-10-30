package com.mobizonetech.aeon_wallet_cursor.data.featureflags

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.preferencesFileName
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class FeatureFlagsTest {

    private fun createTestDataStore(): DataStore<Preferences> {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val testDir = File(context.cacheDir, "datastore-test").apply { mkdirs() }
        return PreferenceDataStoreFactory.create(
            produceFile = { File(testDir, "flags.preferences_pb") }
        )
    }

    @Test
    fun default_values_are_returned_and_can_be_changed() = runTest {
        val dataStore = createTestDataStore()
        val flags = FeatureFlagsImpl(dataStore)

        // Defaults
        val mockDefault = flags.isEnabled(Feature.MOCK_INTERCEPTOR).first()
        val autoDefault = flags.isEnabled(Feature.AUTO_ADVANCE_SLIDES).first()
        assertThat(mockDefault).isTrue()
        assertThat(autoDefault).isFalse()

        // Change values
        flags.set(Feature.AUTO_ADVANCE_SLIDES, true)
        val newAuto = flags.isEnabled(Feature.AUTO_ADVANCE_SLIDES).first()
        assertThat(newAuto).isTrue()
    }
}


