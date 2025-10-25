package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    PAYMENT,
    TRANSACTION,
    PROMOTION,
    SECURITY,
    GENERAL
}

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Mock data - in real app, this would come from API
                val mockNotifications = listOf(
                    NotificationItem(
                        id = "1",
                        title = "Payment Received",
                        message = "You received $50.00 from John Smith",
                        timestamp = "2 hours ago",
                        isRead = false,
                        type = NotificationType.PAYMENT
                    ),
                    NotificationItem(
                        id = "2",
                        title = "Card Transaction",
                        message = "Your card ending in 9012 was used for $25.50",
                        timestamp = "5 hours ago",
                        isRead = true,
                        type = NotificationType.TRANSACTION
                    ),
                    NotificationItem(
                        id = "3",
                        title = "Special Offer",
                        message = "Get 2% cashback on all purchases this month",
                        timestamp = "1 day ago",
                        isRead = false,
                        type = NotificationType.PROMOTION
                    ),
                    NotificationItem(
                        id = "4",
                        title = "Security Alert",
                        message = "New login detected from unknown device",
                        timestamp = "2 days ago",
                        isRead = true,
                        type = NotificationType.SECURITY
                    ),
                    NotificationItem(
                        id = "5",
                        title = "Account Update",
                        message = "Your profile has been updated successfully",
                        timestamp = "3 days ago",
                        isRead = true,
                        type = NotificationType.GENERAL
                    )
                )
                
                _notifications.value = mockNotifications
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load notifications"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                val updatedNotifications = _notifications.value.map { notification ->
                    if (notification.id == notificationId) {
                        notification.copy(isRead = true)
                    } else {
                        notification
                    }
                }
                _notifications.value = updatedNotifications
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to mark notification as read"
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                val updatedNotifications = _notifications.value.map { notification ->
                    notification.copy(isRead = true)
                }
                _notifications.value = updatedNotifications
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to mark all notifications as read"
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                val updatedNotifications = _notifications.value.filter { it.id != notificationId }
                _notifications.value = updatedNotifications
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete notification"
            }
        }
    }

    fun getUnreadCount(): Int {
        return _notifications.value.count { !it.isRead }
    }

    fun refreshNotifications() {
        loadNotifications()
    }

    fun clearError() {
        _error.value = null
    }
}
