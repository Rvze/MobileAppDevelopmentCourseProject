package com.tsypk.corestuff.controller.dto.stuff.response

import com.tsypk.corestuff.controller.dto.stuff.StuffType

data class StuffSearchResponse(
    val stuffType: StuffType,
    val modelId: String,
    val title: String,
    val properties: List<Property>,
    val supplierPrices: ArrayList<SupplierPrice>
)
