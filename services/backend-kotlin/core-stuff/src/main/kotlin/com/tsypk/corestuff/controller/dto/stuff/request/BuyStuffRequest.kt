package com.tsypk.corestuff.controller.dto.stuff.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tsypk.corestuff.controller.dto.stuff.StuffType

data class BuyStuffRequest(
    @JsonProperty(value = "supplier_id")
    val supplierId: Long,
    @JsonProperty(value = "stuff_type")
    val stuffType: StuffType,
    @JsonProperty(value = "model_id")
    val modelId: String,
    @JsonProperty(value = "count")
    val count: Int
) {
}