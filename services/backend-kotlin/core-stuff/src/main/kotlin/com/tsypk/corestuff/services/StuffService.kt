package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequestEvent
import com.tsypk.corestuff.controller.dto.stuff.request.TextRequest
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.exception.RecognitionException
import com.tsypk.corestuff.repository.UserRepository
import com.tsypk.corestuff.services.apple.airpods.AirPodsService
import com.tsypk.corestuff.services.apple.iphone.IphoneService
import com.tsypk.corestuff.services.apple.macbook.MacBookService
import com.tsypk.corestuff.services.messaging.BuyRequestPublisher
import com.tsypk.corestuff.util.buildResponse
import com.tsypk.corestuff.util.toUsersStuff
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StuffService(
    private val airPodsService: AirPodsService,
    private val iphoneService: IphoneService,
    private val macbookService: MacBookService,

    private val userRepository: UserRepository,

    private val recognitionService: RecognitionService,
    private val buyRequestPublisher: BuyRequestPublisher,
) {
    @Transactional
    fun searchByText(textRequest: TextRequest): List<StuffSearchResponse> {
        val recognized = recognitionService.recognizeSearchModels(textRequest.text)
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
        buyRequestPublisher.publish(
            buyStuffRequestEvent = BuyStuffRequestEvent(
                buyerId = userId,
                supplierId = buyStuffRequest.supplierId,
                modelId = buyStuffRequest.modelId,
                count = buyStuffRequest.count,
            )
        )
    }
}