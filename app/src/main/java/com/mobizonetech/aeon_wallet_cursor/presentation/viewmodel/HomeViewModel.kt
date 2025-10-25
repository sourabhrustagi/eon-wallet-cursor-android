package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCardsUseCase: GetCardsUseCase,
    private val getUnlockedCardsUseCase: GetUnlockedCardsUseCase,
    private val unlockCardUseCase: UnlockCardUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val _unlockedCards = MutableStateFlow<Set<String>>(emptySet())
    val unlockedCards: StateFlow<Set<String>> = _unlockedCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCards()
        observeUnlockedCards()
    }

    private fun loadCards() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val cardsList = getCardsUseCase()
                _cards.value = cardsList
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load cards"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun observeUnlockedCards() {
        viewModelScope.launch {
            getUnlockedCardsUseCase().collect { unlockedCardsSet ->
                _unlockedCards.value = unlockedCardsSet
            }
        }
    }

    fun getCombinedCards(): List<Card> {
        val allCards = _cards.value
        val unlockedCardsList = allCards.filter { _unlockedCards.value.contains(it.id) }
        val lockedCardsList = allCards.filter { !unlockedCardsList.contains(it) }
        return unlockedCardsList + lockedCardsList
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
