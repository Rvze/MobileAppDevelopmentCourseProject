package com.tsypk.corestuff.services.apple.airpods

import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.repository.apple.airpods.SupplierAirPodsRepository
import org.springframework.stereotype.Service

@Service
class AirPodsService(
    private val supplierAirPodsRepository: SupplierAirPodsRepository,
) {
    fun updateAllForSupplier(supplierId: Long, airpods: List<SupplierAirPods>) {
        supplierAirPodsRepository.deleteAllBySupplierId(supplierId)
        supplierAirPodsRepository.batchInsertForSupplier(supplierId, airpods)
    }

    fun findBestPrices(request: AirPodsFindBestRequest): List<SupplierAirPods> {
        val a=supplierAirPodsRepository.findByModelAndColor(
            model = request.model,
            color = request.color,
        )
        return if (request.country == null) {
            supplierAirPodsRepository.findByModelAndColor(
                model = request.model,
                color = request.color,
            )
        } else {
            supplierAirPodsRepository.findByModelAndColorWithCountry(
                model = request.model,
                color = request.color,
                country = request.country,
            )
        }
    }

    fun getAll(): List<SupplierAirPods> {
        return supplierAirPodsRepository.getAll()
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        supplierAirPodsRepository.truncateTable()
    }
}
