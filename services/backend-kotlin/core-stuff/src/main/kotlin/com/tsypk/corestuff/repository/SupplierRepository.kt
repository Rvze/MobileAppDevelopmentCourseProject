package com.tsypk.corestuff.repository

import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.repository.apple.airpods.SupplierAirPodsRepository
import com.tsypk.corestuff.repository.apple.iphone.SupplierIphoneRepository
import com.tsypk.corestuff.repository.apple.macbook.SupplierMacBookRepository
import com.tsypk.corestuff.util.buildResponse
import org.springframework.stereotype.Repository

@Repository
class SupplierRepository(
    private val iphoneSupplierRepository: SupplierIphoneRepository,
    private val airpodsSupplierRepository: SupplierAirPodsRepository,
    private val macbookSupplierRepository: SupplierMacBookRepository
) {
    fun getSupplierStuff(userId: Long): List<StuffSearchResponse> {
        val supplierIphones = iphoneSupplierRepository.getAllBySupplierId(userId)
        val supplierAirpods = airpodsSupplierRepository.getAllBySupplierId(userId)
        val supplierMacbooks = macbookSupplierRepository.getAllBySupplierId(userId)

        return buildResponse(supplierAirpods, supplierIphones, supplierMacbooks)
    }
}