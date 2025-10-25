package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetLoansUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedLoansUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockLoanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoansViewModel(
    private val getLoansUseCase: GetLoansUseCase,
    private val getUnlockedLoansUseCase: GetUnlockedLoansUseCase,
    private val unlockLoanUseCase: UnlockLoanUseCase
) : ViewModel() {

    private val _loans = MutableStateFlow<List<Loan>>(emptyList())
    val loans: StateFlow<List<Loan>> = _loans.asStateFlow()

    private val _unlockedLoans = MutableStateFlow<Set<String>>(emptySet())
    val unlockedLoans: StateFlow<Set<String>> = _unlockedLoans.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadLoans()
        observeUnlockedLoans()
    }

    private fun loadLoans() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val loansList = getLoansUseCase()
                _loans.value = loansList
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load loans"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun observeUnlockedLoans() {
        viewModelScope.launch {
            getUnlockedLoansUseCase().collect { unlockedLoansSet ->
                _unlockedLoans.value = unlockedLoansSet
            }
        }
    }

    fun getCombinedLoans(): List<Loan> {
        val allLoans = _loans.value
        val unlockedLoansList = allLoans.filter { _unlockedLoans.value.contains(it.id) }
        val lockedLoansList = allLoans.filter { !unlockedLoansList.contains(it) }
        return unlockedLoansList + lockedLoansList
    }

    fun unlockLoan(loanId: String) {
        viewModelScope.launch {
            try {
                unlockLoanUseCase(loanId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to unlock loan"
            }
        }
    }

    fun refreshLoans() {
        loadLoans()
    }

    fun clearError() {
        _error.value = null
    }
}
