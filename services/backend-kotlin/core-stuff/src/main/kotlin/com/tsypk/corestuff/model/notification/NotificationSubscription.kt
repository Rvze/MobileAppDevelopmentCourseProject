package com.tsypk.corestuff.model.notification

data class NotificationSubscription(
    val id: Long = 0L,
    val userId: Long,
    val entityId: String,
    val type: NotificationType,
)
