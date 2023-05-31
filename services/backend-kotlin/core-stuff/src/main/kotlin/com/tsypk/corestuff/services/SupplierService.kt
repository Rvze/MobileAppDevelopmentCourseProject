package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.stuff.request.TextRequest
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.exception.RecognitionException
import com.tsypk.corestuff.repository.SupplierRepository
import com.tsypk.corestuff.services.apple.airpods.AirPodsService
import com.tsypk.corestuff.services.apple.iphone.IphoneService
import com.tsypk.corestuff.services.apple.macbook.MacBookService
import com.tsypk.corestuff.util.toSupplierAirpods
import com.tsypk.corestuff.util.toSupplierIphone
import com.tsypk.corestuff.util.toSupplierMacbook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository,
    private val recognitionService: RecognitionService,

    private val airPodsService: AirPodsService,
    private val iphoneService: IphoneService,
    private val macbookService: MacBookService

) {

    fun getSupplierStuffById(supplierId: Long): List<StuffSearchResponse> {
        return supplierRepository.getSupplierStuff(supplierId)
    }

    @Transactional
    fun addStuffToSupplier(supplierId: Long, textRequest: TextRequest) {
        val recognized = recognitionService.recognize(textRequest.text)
        if (recognized.allEmpty())
            throw RecognitionException(errorMsg = recognized.errors.toString())
        airPodsService.updateAllForSupplier(supplierId, recognized.airPods.map { it.toSupplierAirpods(supplierId) })
        iphoneService.updateAllForSupplier(supplierId, recognized.iphones.map { it.toSupplierIphone(supplierId) })
        macbookService.updateAllForSupplier(supplierId, recognized.macbooks.map { it.toSupplierMacbook(supplierId) })
    }

}