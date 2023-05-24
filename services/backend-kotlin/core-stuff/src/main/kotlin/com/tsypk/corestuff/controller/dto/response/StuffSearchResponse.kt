package com.tsypk.corestuff.controller.dto.response

data class StuffSearchResponse(
    val stuffType: String,
    val modelId: String,
    val title: String,
    val properties: List<Property>,
    val supplierPrices: ArrayList<SupplierPrice>
)
