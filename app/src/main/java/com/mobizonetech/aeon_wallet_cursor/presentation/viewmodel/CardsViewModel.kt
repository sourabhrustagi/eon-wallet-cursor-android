package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetUnlockedCardsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.UnlockCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CardsViewModel(
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
    
    init {
        loadCards()
        observeUnlockedCards()
    }
    
    private fun loadCards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cardsList = getCardsUseCase()
                _cards.value = cardsList
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun observeUnlockedCards() {
        viewModelScope.launch {
            getUnlockedCardsUseCase().collect { unlockedSet ->
                _unlockedCards.value = unlockedSet
            }
        }
    }
    
    fun unlockCard(cardId: String) {
        viewModelScope.launch {
            try {
                unlockCardUseCase(cardId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun getCombinedCards(): List<Card> {
        val cardsList = _cards.value
        val unlockedSet = _unlockedCards.value
        
        val unlockedCardsList = cardsList.filter { unlockedSet.contains(it.id) }
        val lockedCardsList = cardsList.filter { !unlockedSet.contains(it.id) }
        
        return unlockedCardsList + lockedCardsList
    }
}
