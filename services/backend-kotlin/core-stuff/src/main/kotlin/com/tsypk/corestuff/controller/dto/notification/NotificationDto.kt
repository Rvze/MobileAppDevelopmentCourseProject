package com.tsypk.corestuff.controller.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty
import com.tsypk.corestuff.model.notification.NotificationStatus

data class NotificationDto(
    @JsonProperty(value = "status")
    val status: NotificationStatus,
    @JsonProperty(value = "text")
    val text: String,
)
