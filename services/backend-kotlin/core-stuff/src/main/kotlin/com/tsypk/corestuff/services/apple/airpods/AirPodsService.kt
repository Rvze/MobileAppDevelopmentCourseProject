package com.tsypk.corestuff.services.apple.airpods

import com.tsypk.corestuff.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.model.notification.PriceUpdate
import com.tsypk.corestuff.model.notification.StuffUpdateEvent
import com.tsypk.corestuff.model.notification.StuffUpdateType
import com.tsypk.corestuff.repository.apple.airpods.SupplierAirPodsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AirPodsService(
    private val repository: SupplierAirPodsRepository,
) {
    @Transactional
    fun updateAllForSupplier(supplierId: Long, airpods: List<SupplierAirPods>): List<StuffUpdateEvent> {
        val before = repository.getAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }
        repository.deleteAllBySupplierId(supplierId)
        repository.batchInsertForSupplier(supplierId, airpods)
        val after = repository.getAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }

        val result = mutableMapOf<String, StuffUpdateEvent>()
        before.forEach {
            val modelId = it.value.first().id
            if (it.key in after) {
                val bef = it.value.first()
                val aft = after[it.key]!!.first()

                if (bef.priceAmount != aft.priceAmount) {
                    result[it.key] = StuffUpdateEvent(
                        type = StuffUpdateType.UPDATE,
                        modelId = modelId,
                        payload = PriceUpdate(
                            oldPrice = bef.priceAmount.toLong(),
                            newPrice = aft.priceAmount.toLong(),
                        )
                    )
                }
            } else {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.DELETE,
                    modelId = modelId,
                )
            }
        }

        after.forEach {
            val modelId = it.value.first().id
            if (it.key !in before) {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.CREATE,
                    modelId = modelId,
                )
            }
        }

        return result.values.toList()
    }

    fun findBestPrices(request: AirPodsFindBestRequest): List<SupplierAirPods> {
        return if (request.country == null) {
            repository.findByModelAndColor(
                model = request.model,
                color = request.color,
            )
        } else {
            repository.findByModelAndColorWithCountry(
                model = request.model,
                color = request.color,
                country = request.country,
            )
        }
    }

    fun getAll(): List<SupplierAirPods> {
        return repository.getAll()
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        repository.truncateTable()
    }
}
