package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val getUnlockedCardsUseCase: GetUnlockedCardsUseCase,
    private val unlockCardUseCase: UnlockCardUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Reactive cards flow that combines cards and unlocked state
    val cards: StateFlow<List<Card>> = getCardsUseCase()
        .map { cardsList ->
            cardsList.map { card ->
                card.copy(isLocked = !getUnlockedCardsUseCase().value.contains(card.id))
            }
        }
        .asStateFlow(initialValue = emptyList())

    // Reactive unlocked cards flow
    val unlockedCards: StateFlow<Set<String>> = getUnlockedCardsUseCase()

    // Combined cards with proper ordering (unlocked first)
    val combinedCards: StateFlow<List<Card>> = combine(
        getCardsUseCase(),
        getUnlockedCardsUseCase()
    ) { cardsList, unlockedSet ->
        val unlockedCards = cardsList.filter { unlockedSet.contains(it.id) }
        val lockedCards = cardsList.filter { !unlockedSet.contains(it.id) }
        unlockedCards + lockedCards
    }.asStateFlow(initialValue = emptyList())

    init {
        loadCards()
    }

    private fun loadCards() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                // Cards are loaded reactively through the flow
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load cards"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun unlockCard(cardId: String) {
        viewModelScope.launch {
            try {
                unlockCardUseCase(cardId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to unlock card"
            }
        }
    }

    fun refreshCards() {
        loadCards()
    }

    fun clearError() {
        _error.value = null
    }
}
