package com.mobizonetech.aeon_wallet_cursor.domain.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.model.User
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun getCards(): List<Card>
    suspend fun getCardById(id: String): Card?
    suspend fun unlockCard(cardId: String)
    suspend fun lockCard(cardId: String)
    fun getUnlockedCards(): Flow<Set<String>>
}

interface LoanRepository {
    suspend fun getLoans(): List<Loan>
    suspend fun getLoanById(id: String): Loan?
    suspend fun unlockLoan(loanId: String)
    suspend fun lockLoan(loanId: String)
    fun getUnlockedLoans(): Flow<Set<String>>
}

interface UserRepository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
    suspend fun logout()
    fun isUserLoggedIn(): Flow<Boolean>
}
