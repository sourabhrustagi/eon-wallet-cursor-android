package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

class GetCardsUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(): List<Card> {
        return cardRepository.getCards()
    }
}

class UnlockCardUseCase(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: String) {
        cardRepository.unlockCard(cardId)
    }
}

class GetUnlockedCardsUseCase(
    private val cardRepository: CardRepository
) {
    operator fun invoke(): Flow<Set<String>> {
        return cardRepository.getUnlockedCards()
    }
}
