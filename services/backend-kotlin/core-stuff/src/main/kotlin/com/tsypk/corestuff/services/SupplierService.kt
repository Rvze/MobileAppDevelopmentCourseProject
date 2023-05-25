package com.tsypk.corestuff.services

import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.repository.SupplierRepository
import org.springframework.stereotype.Service

@Service
class SupplierService(
    private val supplierRepository: SupplierRepository
) {

    fun getSupplierStuffById(supplierId: Long): List<StuffSearchResponse> {
        return supplierRepository.getSupplierStuff(supplierId)
    }
}