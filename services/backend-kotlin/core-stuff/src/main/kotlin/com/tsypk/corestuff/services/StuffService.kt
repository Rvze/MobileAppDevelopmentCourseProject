package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.SearchByTextRequest
import com.tsypk.corestuff.controller.dto.SearchModelsRecognitionResult
import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.response.Price
import com.tsypk.corestuff.controller.dto.response.Property
import com.tsypk.corestuff.controller.dto.response.StuffSearchResponse
import com.tsypk.corestuff.controller.dto.response.SupplierPrice
import com.tsypk.corestuff.exception.RecognitionException
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.model.apple.SupplierMacbook
import com.tsypk.corestuff.services.apple.airpods.AirPodsService
import com.tsypk.corestuff.services.apple.iphone.IphoneService
import com.tsypk.corestuff.services.apple.macbook.MacBookService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import recognitioncommons.exception.StaffRecognitionException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.service.recognition.apple.airpods.AirPodsRecognitionService
import recognitioncommons.service.recognition.apple.iphone.IphoneRecognitionService
import recognitioncommons.service.recognition.apple.macbook.MacbookRecognitionService
import recognitioncommons.util.Presentation.AirPodsPresentation.toHumanReadableString
import recognitioncommons.util.Presentation.IphonePresentation.toHumanReadableString
import recognitioncommons.util.Presentation.MacbookPresentation.toHumanReadableString
import recognitioncommons.util.normalization.normalizeText

@Service
class StuffService(
    private val airPodsService: AirPodsService,
    private val iphoneService: IphoneService,
    private val macbookService: MacBookService,

    private val airPodsRecognitionService: AirPodsRecognitionService,
    private val macbookRecognitionService: MacbookRecognitionService,
    private val iphoneRecognitionService: IphoneRecognitionService
) {
    @Transactional
    fun searchByText(searchByTextRequest: SearchByTextRequest): List<StuffSearchResponse> {
        val recognized = recognizeSearchModels(searchByTextRequest.text)
        if (recognized.allEmpty())
            throw RecognitionException(errorMsg = recognized.errorsToString())
        val result: ArrayList<StuffSearchResponse> = arrayListOf()
        recognized.airPods.forEach {
            val airpodsSearchModel = AirPodsFindBestRequest(
                model = it.model,
                color = it.color,
                country = it.country
            )
            result.addAll(buildResponse(airPodsService.findBestPrices(airpodsSearchModel), null, null))
        }
        recognized.iphones.forEach {
            val iphoneFindBestRequest = IphonesFindBestRequest(
                model = it.model,
                color = it.color,
                memory = it.memory,
                country = it.country
            )
            result.addAll(buildResponse(null, iphoneService.findBestPrices(iphoneFindBestRequest), null))
        }
        //TODO
        recognized.macbooks.forEach {
            val macbookFindBestRequest = MacbookFindBestRequest(
                model = it.model,
                ram = it.ram,
                memory = it.memory,
                color = it.color,
                country = it.country
            )
            result.addAll(buildResponse(null, null, macbookService.getFindPrices(macbookFindBestRequest)))

        }
        return result
    }

    private fun recognizeSearchModels(text: String): SearchModelsRecognitionResult {
        val result = SearchModelsRecognitionResult()
        normalizeText(text).forEach {
            try {
                if (airPodsRecognitionService.containsAirPodsInLine(it)) {
                    val recognized = airPodsRecognitionService.recognizeSearchModel(it)
                    result.airPods.addAll(recognized)
                } else if (macbookRecognitionService.containsMacbookInLine(it)) {
                    val recognized = macbookRecognitionService.recognizeSearchModel(it)
                    result.macbooks.addAll(recognized)
                } else {
                    val recognized = iphoneRecognitionService.recognizeSearchModel(it)
                    result.iphones.addAll(recognized)
                }
            } catch (e: StaffRecognitionException) {
                result.errors.add(
                    "$it - ${staffRecognitionExceptionToHumanReadable(e)}"
                )
            }
        }
        return result.copy(
            iphones = result.iphones.toSet().toMutableList(),
            airPods = result.airPods.toSet().toMutableList(),
            macbooks = result.macbooks.toSet().toMutableList(),
            errors = result.errors.toSet().toMutableList()
        )
    }

    private fun buildResponse(
        supplierAirPods: List<SupplierAirPods>?,
        supplierIphones: List<SupplierIphone>?,
        supplierMacbooks: List<SupplierMacbook>?
    ): List<StuffSearchResponse> {
        val stuffSearchResponses: ArrayList<StuffSearchResponse> = arrayListOf()
        if (!supplierAirPods.isNullOrEmpty()) {
            val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
            supplierAirPods.forEach {
                val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
                if (!visited.containsKey(it.id)) {
                    val stuffSearchResponse = StuffSearchResponse(
                        modelId = it.airPodsFullModel.toString(),
                        stuffType = "AIRPODS",
                        title = it.airPodsFullModel.model.toHumanReadableString(),
                        properties = listOf(),
                        supplierPrices = arrayListOf(price)
                    )
                    visited[it.id] = stuffSearchResponse
                } else {
                    visited[it.id]!!.supplierPrices.add(price)
                }
            }
            stuffSearchResponses.addAll(visited.values)
        }
        if (!supplierIphones.isNullOrEmpty()) {
            val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
            supplierIphones.forEach {
                val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
                if (!visited.containsKey(it.id)) {
                    val memoryProperty = Property("MEMORY", it.iphoneFullModel.memory.toString())
                    val colorProperty = Property("COLOR", it.iphoneFullModel.color.toString())
                    val stuffSearchResponse = StuffSearchResponse(
                        modelId = it.iphoneFullModel.toString(),
                        stuffType = "IPHONES",
                        title = it.iphoneFullModel.model.toHumanReadableString(),
                        properties = listOf(memoryProperty, colorProperty),
                        supplierPrices = arrayListOf(price)
                    )
                    visited[it.id] = stuffSearchResponse
                } else {
                    visited[it.id]!!.supplierPrices.add(price)
                }
            }
            stuffSearchResponses.addAll(visited.values)
        }
        if (!supplierMacbooks.isNullOrEmpty()) {
            val visited: HashMap<String, StuffSearchResponse> = hashMapOf()
            supplierMacbooks.forEach {
                val price = SupplierPrice(it.supplierId, Price(it.priceAmount.toDouble(), it.priceCurrency))
                if (!visited.containsKey(it.macId)) {
                    val screenPropery = Property("SCREEN", it.macbookFullModel.model.screen.toString())
                    val memoryProperty = Property(name = "MEMORY", it.macbookFullModel.memory.toString())
                    val cpuProperty = Property("CHIP", it.macbookFullModel.model.appleChip.toString())
                    val ramProperty = Property("RAM", it.macbookFullModel.ram.toString())
                    val colorProperty = Property("COLOR", it.macbookFullModel.color.toString())
                    val stuffSearchResponse = StuffSearchResponse(
                        modelId = it.macId,
                        stuffType = "MACBOOKS",
                        title = it.macbookFullModel.model.toHumanReadableString(),
                        properties = listOf(screenPropery, memoryProperty, cpuProperty, ramProperty, colorProperty),
                        supplierPrices = arrayListOf(price)
                    )
                    visited[it.macId] = stuffSearchResponse
                } else {
                    visited[it.macId]!!.supplierPrices.add(price)
                }
            }
            stuffSearchResponses.addAll(visited.values)
        }
        return stuffSearchResponses
    }
}