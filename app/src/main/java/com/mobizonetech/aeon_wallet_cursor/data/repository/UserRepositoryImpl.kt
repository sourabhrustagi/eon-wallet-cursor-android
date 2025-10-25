package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.data.UserData
import com.mobizonetech.aeon_wallet_cursor.data.UserPreferences
import com.mobizonetech.aeon_wallet_cursor.domain.model.User
import com.mobizonetech.aeon_wallet_cursor.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : UserRepository {

    override suspend fun getUser(): User? {
        return userPreferences.userData.value.let { userData ->
            if (userData.id.isNotEmpty()) {
                User(
                    id = userData.id,
                    name = userData.name,
                    email = userData.email,
                    phoneNumber = userData.phoneNumber,
                    cardNumber = userData.cardNumber,
                    cardType = userData.cardType,
                    passcode = userData.passcode,
                    securityPasscode = userData.securityPasscode
                )
            } else null
        }
    }

    override suspend fun saveUser(user: User) {
        val userData = UserData(
            id = user.id,
            name = user.name,
            email = user.email,
            phoneNumber = user.phoneNumber,
            cardNumber = user.cardNumber,
            cardType = user.cardType,
            passcode = user.passcode,
            securityPasscode = user.securityPasscode
        )
        userPreferences.setLoggedIn(userData)
    }

    override suspend fun logout() {
        userPreferences.logout()
    }

    override fun isUserLoggedIn(): Flow<Boolean> {
        return userPreferences.isLoggedIn
    }
}
