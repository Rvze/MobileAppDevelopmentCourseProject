package com.tsypk.corestuff.controller.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationStuffRequest(
    @JsonProperty("action_type")
    val actionType: NotificationActionType,
    @JsonProperty("model_id")
    val modelId: String,
)
