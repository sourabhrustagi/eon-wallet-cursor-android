package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.repository.LoanRepository
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import kotlinx.coroutines.flow.Flow

class LoanRepositoryImpl(
    private val unlockPreferences: UnlockPreferences
) : LoanRepository {
    
    override suspend fun getLoans(): List<Loan> {
        return listOf(
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
