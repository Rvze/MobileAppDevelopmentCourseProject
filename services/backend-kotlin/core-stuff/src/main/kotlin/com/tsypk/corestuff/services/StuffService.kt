package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.stuff.request.SearchByTextRequest
import com.tsypk.corestuff.controller.dto.SearchModelsRecognitionResult
import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.stuff.response.Price
import com.tsypk.corestuff.controller.dto.stuff.response.Property
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.controller.dto.stuff.response.SupplierPrice
import com.tsypk.corestuff.controller.dto.stuff.StuffType
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import com.tsypk.corestuff.exception.RecognitionException
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.model.apple.SupplierMacbook
import com.tsypk.corestuff.repository.UserRepository
import com.tsypk.corestuff.services.apple.airpods.AirPodsService
import com.tsypk.corestuff.services.apple.iphone.IphoneService
import com.tsypk.corestuff.services.apple.macbook.MacBookService
import com.tsypk.corestuff.util.buildResponse
import com.tsypk.corestuff.util.toUsersStuff
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
    private val iphoneRecognitionService: IphoneRecognitionService,

    private val userRepository: UserRepository
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

    fun buyStuff(buyStuffRequest: BuyStuffRequest, userId: Long) {
        userRepository.saveUserStuff(buyStuffRequest.toUsersStuff(userId))
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

}