package com.mobizonetech.aeon_wallet_cursor.util

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TestViewModel : ViewModel()

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineExtensionsTest {

    @Test
    fun launchSafe_executes_block_and_catches_errors() = runTest {
        val vm = TestViewModel()
        val dispatcher = object : DispatcherProvider {
            override val io = StandardTestDispatcher(testScheduler)
            override val main = io
            override val default = io
        }

        var onErrorCalled = false
        var executed = false

        vm.launchSafe(dispatcher, block = {
            executed = true
            throw IllegalStateException("boom")
        }, onError = {
            onErrorCalled = true
            assertThat(it).isInstanceOf(IllegalStateException::class.java)
        })

        // Advance until idle
        (dispatcher.io as StandardTestDispatcher).scheduler.advanceUntilIdle()

        assertThat(executed).isTrue()
        assertThat(onErrorCalled).isTrue()
    }
}


