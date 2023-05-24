package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.SearchModelsRecognitionResult
import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.response.Price
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
    fun searchByText(text: String): List<StuffSearchResponse> {
        val recognized = recognizeSearchModels(text)
        if (recognized.allEmpty())
            throw RecognitionException(errorMsg = recognized.errorsToString())
        val supplierAirPods: ArrayList<SupplierAirPods> = arrayListOf()
        val supplierIphones: ArrayList<SupplierIphone> = arrayListOf()
        val supplierMacbooks: ArrayList<SupplierMacbook> = arrayListOf()
        recognized.airPods.forEach {
            val airpodsSearchModel = AirPodsFindBestRequest(
                model = it.model,
                color = it.color,
                country = it.country
            )
            buildResponse(airPodsService.findBestPrices(airpodsSearchModel), null, null)
        }
        recognized.iphones.forEach {
            val iphoneFindBestRequest = IphonesFindBestRequest(
                model = it.model,
                color = it.color,
                memory = it.memory,
                country = it.country
            )
            supplierIphones.addAll(iphoneService.findBestPrices(iphoneFindBestRequest))
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
        }
        return buildResponse(supplierAirPods, supplierIphones, supplierMacbooks)
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
        return stuffSearchResponses
    }
}