package com.tsypk.corestuff.services.sony.playstation

import com.tsypk.corestuff.controller.dto.playstation.PlayStationFindBestPricesDto
import com.tsypk.corestuff.model.sony.SupplierPlaystation
import com.tsypk.corestuff.repository.sony.playstation.SupplierPlaystationRepository
import org.springframework.stereotype.Service

@Service
class PlaystationService(
    private val supplierPlaystationRepository: SupplierPlaystationRepository
) {
    fun updateAllForSupplier(supplierId: Long, args: List<SupplierPlaystation>) {
        supplierPlaystationRepository.deleteAllBySupplierId(supplierId)
        supplierPlaystationRepository.batchInsertOrUpdate(supplierId, args)
    }

    fun getAll(): List<SupplierPlaystation> {
        return supplierPlaystationRepository.getAll()
    }

    //TODO
    fun findBestPrices(request: PlayStationFindBestPricesDto): List<SupplierPlaystation> {
        return listOf()
    }
}