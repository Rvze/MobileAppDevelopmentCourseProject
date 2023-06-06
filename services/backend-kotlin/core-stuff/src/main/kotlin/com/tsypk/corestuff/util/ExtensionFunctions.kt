package com.tsypk.corestuff.util

import com.tsypk.corestuff.controller.dto.airpods.AirPodsDto
import com.tsypk.corestuff.controller.dto.airpods.SupplierAirPodsDto
import com.tsypk.corestuff.controller.dto.iphone.IphoneDto
import com.tsypk.corestuff.controller.dto.iphone.SupplierIphoneDto
import com.tsypk.corestuff.controller.dto.macbook.MacbookDto
import com.tsypk.corestuff.controller.dto.macbook.SupplierMacbookDto
import com.tsypk.corestuff.controller.dto.stuff.StuffType
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import com.tsypk.corestuff.controller.dto.stuff.response.Price
import com.tsypk.corestuff.controller.dto.stuff.response.Property
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.controller.dto.stuff.response.SupplierPrice
import com.tsypk.corestuff.exception.NotAirPodsIdException
import com.tsypk.corestuff.exception.NotIphoneIdException
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.model.apple.SupplierMacbook
import com.tsypk.corestuff.model.stuff.UsersStuff
import recognitioncommons.exception.InvalidAirPodsIdException
import recognitioncommons.exception.InvalidIphoneIdException
import recognitioncommons.exception.InvalidMacbookIdException
import recognitioncommons.exception.NotMacbookException
import recognitioncommons.models.Money
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookFullModel
import recognitioncommons.models.country.Country
import recognitioncommons.util.Presentation.AirPodsPresentation.toHumanReadableString
import recognitioncommons.util.Presentation.IphonePresentation.toHumanReadableString
import recognitioncommons.util.Presentation.MacbookPresentation.toHumanReadableString
import recognitioncommons.util.extractor.airPodsFullModelFromId
import recognitioncommons.util.extractor.macbookFullModelFromId
import recognitioncommons.util.idString
import java.math.BigDecimal

/**
 * Iphone
 */
fun IphoneDto.toSupplierIphone(supplierId: Long): SupplierIphone =
    SupplierIphone(
        id = this.id,
        supplierId = supplierId,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency,
    )

fun SupplierIphone.toSupplierIphoneDto(): SupplierIphoneDto =
    SupplierIphoneDto(
        id = this.id,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(
            amount = this.priceAmount,
            currency = this.priceCurrency,
        ),
    )

fun Iphone.toSupplierIphone(supplierId: Long): SupplierIphone =
    SupplierIphone(
        id = this.idString(),
        supplierId = supplierId,
        country = this.country,
        priceAmount = BigDecimal(this.price)
    )

fun iphoneFullModelFromId(input: String, country: Country): IphoneFullModel {
    try {
        return recognitioncommons.util.extractor.iphoneFullModelFromId(input, country)
    } catch (e: InvalidIphoneIdException) {
        throw NotIphoneIdException(input)
    }
}

/**
 * AirPods
 */
fun AirPodsDto.toSupplierAirPods(supplierId: Long): SupplierAirPods =
    SupplierAirPods(
        supplierId = supplierId,
        id = this.id,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency
    )

fun SupplierAirPods.toSupplierAirPodsDto(): SupplierAirPodsDto =
    SupplierAirPodsDto(
        id = this.id,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(
            amount = this.priceAmount,
            currency = this.priceCurrency,
        ),
    )

fun AirPods.toSupplierAirpods(supplierId: Long): SupplierAirPods =
    SupplierAirPods(
        supplierId = supplierId,
        id = this.idString(),
        country = this.country,
        priceAmount = BigDecimal(this.price),
    )

fun airPodsFullModelFromId(input: String, country: Country): AirPodsFullModel {
    try {
        return airPodsFullModelFromId(input, country)
    } catch (e: InvalidAirPodsIdException) {
        throw NotAirPodsIdException(input)
    }
}

/**
 * Macbook
 */
fun MacbookDto.toSupplierMacbookMapper(supplierId: Long): SupplierMacbook =
    SupplierMacbook(
        supplierId = supplierId,
        id = this.id,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency
    )

fun SupplierMacbook.toSupplierMacbookDtoMapper(): SupplierMacbookDto =
    SupplierMacbookDto(
        id = this.id,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(this.priceAmount, this.priceCurrency)
    )

fun Macbook.toSupplierMacbook(supplierId: Long): SupplierMacbook =
    SupplierMacbook(
        id = this.idString(),
        supplierId = supplierId,
        country = this.country,
        priceAmount = BigDecimal(this.price)
    )

fun macbookFullModelFromId(input: String): MacbookFullModel {
    try {
        return macbookFullModelFromId(input)
    } catch (e: InvalidMacbookIdException) {
        throw NotMacbookException(input)
    }
}

/**
 * Users
 */
fun BuyStuffRequest.toUsersStuff(userId: Long): UsersStuff =
    UsersStuff(
        userId = userId,
        stuffId = this.modelId,
        supplierId = this.supplierId,
        count = this.count
    )

fun buildResponse(
    supplierAirPods: List<SupplierAirPods>?,
    supplierIphones: List<SupplierIphone>?,
    supplierMacbooks: List<SupplierMacbook>?
): List<StuffSearchResponse> {
    val stuffSearchResponses: ArrayList<StuffSearchResponse> = arrayListOf()
    if (!supplierAirPods.isNullOrEmpty()) {
        val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
        supplierAirPods.forEach {
            val modelId = "${it.id}/${it.country.name}"
            val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
            if (!visited.containsKey(modelId)) {
                val stuffSearchResponse = StuffSearchResponse(
                    modelId = modelId,
                    stuffType = StuffType.AIRPODS,
                    title = it.airPodsFullModel.model.toHumanReadableString(),
                    properties = listOf(
                        Property("COUNTRY", it.country.nameRu),
                    ),
                    supplierPrices = arrayListOf(price)
                )
                visited[modelId] = stuffSearchResponse
            } else {
                visited[modelId]!!.supplierPrices.add(price)
            }
        }
        stuffSearchResponses.addAll(visited.values)
    }
    if (!supplierIphones.isNullOrEmpty()) {
        val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
        supplierIphones.forEach {
            val modelId = "${it.id}/${it.country.name}"
            val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
            if (!visited.containsKey(modelId)) {
                val memoryProperty = Property("MEMORY", it.iphoneFullModel.memory.toString())
                val colorProperty = Property("COLOR", it.iphoneFullModel.color.toString())
                val countryProperty = Property("COUNTRY", it.country.nameRu)
                val stuffSearchResponse = StuffSearchResponse(
                    modelId = modelId,
                    stuffType = StuffType.IPHONE,
                    title = it.iphoneFullModel.model.toHumanReadableString(),
                    properties = listOf(memoryProperty, colorProperty, countryProperty),
                    supplierPrices = arrayListOf(price)
                )
                visited[modelId] = stuffSearchResponse
            } else {
                visited[modelId]!!.supplierPrices.add(price)
            }
        }
        stuffSearchResponses.addAll(visited.values)
    }
    if (!supplierMacbooks.isNullOrEmpty()) {
        val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
        supplierMacbooks.forEach {
            val modelId = "${it.id}/${it.country.name}"
            val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
            if (!visited.containsKey(modelId)) {
                val screenPropery = Property("SCREEN", it.macbookFullModel.model.screen.toString())
                val memoryProperty = Property(name = "MEMORY", it.macbookFullModel.memory.toString())
                val cpuProperty = Property("CHIP", it.macbookFullModel.model.appleChip.toString())
                val ramProperty = Property("RAM", it.macbookFullModel.ram.toString())
                val colorProperty = Property("COLOR", it.macbookFullModel.color.toString())
                val countryProperty = Property("COUNTRY", it.country.nameRu)
                val stuffSearchResponse = StuffSearchResponse(
                    modelId = modelId,
                    stuffType = StuffType.MACBOOK,
                    title = it.macbookFullModel.model.toHumanReadableString(),
                    properties = listOf(
                        screenPropery,
                        memoryProperty,
                        cpuProperty,
                        ramProperty,
                        colorProperty,
                        countryProperty
                    ),
                    supplierPrices = arrayListOf(price)
                )
                visited[modelId] = stuffSearchResponse
            } else {
                visited[modelId]!!.supplierPrices.add(price)
            }
        }
        stuffSearchResponses.addAll(visited.values)
    }
    return stuffSearchResponses
}