package com.mobizonetech.aeon_wallet_cursor.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val welcomeRepository: WelcomeRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            when (welcomeRepository.getWelcomeSlides()) {
                is Result.Success -> Result.success()
                else -> Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}


