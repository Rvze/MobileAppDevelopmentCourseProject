package com.tsypk.corestuff.services.apple.iphone

import com.tsypk.corestuff.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.model.notification.PriceUpdate
import com.tsypk.corestuff.model.notification.StuffUpdateEvent
import com.tsypk.corestuff.model.notification.StuffUpdateType
import com.tsypk.corestuff.repository.apple.iphone.SupplierIphoneRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IphoneService(
    private val repository: SupplierIphoneRepository,
) {
    @Transactional
    fun updateAllForSupplier(supplierId: Long, iphones: List<SupplierIphone>): List<StuffUpdateEvent> {
        val before = repository.getAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }
        repository.deleteAllBySupplierId(supplierId)
        repository.batchInsertForSupplier(supplierId, iphones)
        val after = repository.getAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }

        val result = mutableMapOf<String, StuffUpdateEvent>()
        before.forEach {
            if (it.key in after) {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.UPDATE,
                    modelId = it.key,
                    payload = PriceUpdate(
                        oldPrice = it.value.first().priceAmount.toLong(),
                        newPrice = after[it.key]!!.first().priceAmount.toLong(),
                    )
                )
            } else {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.DELETE,
                    modelId = it.key,
                )
            }
        }

        after.forEach {
            if (it.key !in after) {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.CREATE,
                    modelId = it.key,
                )
            }
        }

        return result.values.toList()
    }

    fun findBestPrices(request: IphonesFindBestRequest): List<SupplierIphone> {
        return repository.getAllLike(request.model, request.memory, request.color, request.country)
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        repository.truncateTable()
    }
}
