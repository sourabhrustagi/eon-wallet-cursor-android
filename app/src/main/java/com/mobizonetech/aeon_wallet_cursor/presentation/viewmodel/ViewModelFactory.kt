package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.data.repository.CardRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.data.repository.LoanRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockCardUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetLoansUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedLoansUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockLoanUseCase

class ViewModelFactory(
    private val unlockPreferences: UnlockPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                val cardRepository = CardRepositoryImpl(unlockPreferences)
                val getCardsUseCase = GetCardsUseCase(cardRepository)
                val getUnlockedCardsUseCase = GetUnlockedCardsUseCase(cardRepository)
                val unlockCardUseCase = UnlockCardUseCase(cardRepository)
                HomeViewModel(getCardsUseCase, getUnlockedCardsUseCase, unlockCardUseCase) as T
            }
            modelClass.isAssignableFrom(LoansViewModel::class.java) -> {
                val loanRepository = LoanRepositoryImpl(unlockPreferences)
                val getLoansUseCase = GetLoansUseCase(loanRepository)
                val getUnlockedLoansUseCase = GetUnlockedLoansUseCase(loanRepository)
                val unlockLoanUseCase = UnlockLoanUseCase(loanRepository)
                LoansViewModel(getLoansUseCase, getUnlockedLoansUseCase, unlockLoanUseCase) as T
            }
            modelClass.isAssignableFrom(CardRepaymentViewModel::class.java) -> {
                val cardRepository = CardRepositoryImpl(unlockPreferences)
                CardRepaymentViewModel(cardRepository) as T
            }
            modelClass.isAssignableFrom(LoanRepaymentViewModel::class.java) -> {
                val loanRepository = LoanRepositoryImpl(unlockPreferences)
                LoanRepaymentViewModel(loanRepository) as T
            }
            modelClass.isAssignableFrom(UtilityPaymentViewModel::class.java) -> {
                UtilityPaymentViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
