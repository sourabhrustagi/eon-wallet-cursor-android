package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.repository.LoanRepository
import kotlinx.coroutines.flow.Flow

class GetLoansUseCase(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(): List<Loan> {
        return loanRepository.getLoans()
    }
}

class UnlockLoanUseCase(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanId: String) {
        loanRepository.unlockLoan(loanId)
    }
}

class GetUnlockedLoansUseCase(
    private val loanRepository: LoanRepository
) {
    operator fun invoke(): Flow<Set<String>> {
        return loanRepository.getUnlockedLoans()
    }
}
