package com.tsypk.corestuff.model.apple

import com.tsypk.corestuff.util.macbookFullModelFromId
import recognitioncommons.models.apple.macbook.MacbookFullModel
import recognitioncommons.models.country.Country
import java.math.BigDecimal
import java.time.Instant

data class SupplierMacbook(
    var macId: String,
    val macbookFullModel: MacbookFullModel = macbookFullModelFromId(macId),
    var supplierId: Long,
    var country: Country,
    var priceAmount: BigDecimal,
    var priceCurrency: String = "RUB"
) {
    val modifiedAt: Instant = Instant.now()
}