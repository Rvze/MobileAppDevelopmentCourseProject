package com.tsypk.corestuff.model.notification

import com.fasterxml.jackson.annotation.JsonProperty

enum class StuffUpdateType {
    UPDATE,
    CREATE,
    DELETE,
}

data class StuffUpdate(
    @JsonProperty("type")
    val type: StuffUpdateType,
    @JsonProperty("model_id")
    val modelId: String,
    @JsonProperty("price")
    val price: Long?
)
