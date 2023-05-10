package com.tsypk.corestuff.services.apple.iphone

import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.repository.apple.iphone.SupplierIphoneRepository
import org.springframework.stereotype.Service

@Service
class IphoneService(
    private val supplierIphoneRepository: SupplierIphoneRepository,
) {
    fun updateAllForSupplier(supplierId: Long, iphones: List<SupplierIphone>) {
        supplierIphoneRepository.deleteAllBySupplierId(supplierId)
        supplierIphoneRepository.batchInsertForSupplier(supplierId, iphones)
    }

    fun findBestPrices(request: IphonesFindBestRequest): List<SupplierIphone> {
        return supplierIphoneRepository.getAllLike(request.model, request.memory, request.color, request.country)
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        supplierIphoneRepository.truncateTable()
    }
}
