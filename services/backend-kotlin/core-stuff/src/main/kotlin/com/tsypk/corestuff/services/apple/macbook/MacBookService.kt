package com.tsypk.corestuff.services.apple.macbook

import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.model.apple.SupplierMacbook
import com.tsypk.corestuff.model.notification.PriceUpdate
import com.tsypk.corestuff.model.notification.StuffUpdateEvent
import com.tsypk.corestuff.model.notification.StuffUpdateType
import com.tsypk.corestuff.repository.apple.macbook.SupplierMacBookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MacBookService(
    private val repository: SupplierMacBookRepository
) {
    @Transactional
    fun updateAllForSupplier(supplierId: Long, macbooks: List<SupplierMacbook>): List<StuffUpdateEvent> {
        val before = repository.getAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }
        repository.deleteAllBySupplierId(supplierId)
        repository.batchUpdateMacbooks(supplierId, macbooks)
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

    fun getFindPrices(request: MacbookFindBestRequest): List<SupplierMacbook> {
        return repository.getAllLike(request)
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        repository.truncateTable()
    }
}