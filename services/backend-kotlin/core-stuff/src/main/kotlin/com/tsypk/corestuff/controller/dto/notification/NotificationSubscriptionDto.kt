package com.tsypk.corestuff.controller.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty
import com.tsypk.corestuff.model.notification.NotificationType

data class NotificationSubscriptionDto(
    @JsonProperty(value = "model_id")
    val modelId: String,
    @JsonProperty(value = "type")
    val type: NotificationType,
)
