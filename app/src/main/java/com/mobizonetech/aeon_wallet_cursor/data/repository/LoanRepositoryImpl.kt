package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.repository.LoanRepository
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.data.LoanDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoanRepositoryImpl @Inject constructor(
    private val unlockPreferences: UnlockPreferences,
    private val loanDataStore: LoanDataStore
) : LoanRepository {
    
    override suspend fun getLoans(): List<Loan> {
        return loanDataStore.getLoans().first()
    }
    
    override suspend fun getLoanById(id: String): Loan? {
        return getLoans().find { it.id == id }
    }
    
    override suspend fun unlockLoan(loanId: String) {
        unlockPreferences.unlockLoan(loanId)
    }
    
    override suspend fun lockLoan(loanId: String) {
        unlockPreferences.lockLoan(loanId)
    }
    
    override fun getUnlockedLoans(): Flow<Set<String>> {
        return unlockPreferences.unlockedLoans
    }
}
