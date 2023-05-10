package com.tsypk.corestuff.services.apple.macbook

import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookUpdateRequest
import com.tsypk.corestuff.controller.dto.macbook.SupplierMacbookDto
import com.tsypk.corestuff.model.apple.SupplierMacbook
import com.tsypk.corestuff.repository.apple.macbook.SupplierMacBookRepository
import com.tsypk.corestuff.util.toSupplierMacbookDtoMapper
import org.springframework.stereotype.Service

@Service
class MacBookService(
    private val macBookRepository: SupplierMacBookRepository
) {
    fun updateAllForSupplier(supplierId: Long, macbooks: List<SupplierMacbook>) {
        macBookRepository.deleteAllBySupplierId(supplierId)
        macBookRepository.batchUpdateMacbooks(supplierId, macbooks)
    }

    fun getFindPrices(request: MacbookFindBestRequest): List<SupplierMacbookDto> {
        return macBookRepository.getAllLike(request).map { it.toSupplierMacbookDtoMapper() }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        macBookRepository.truncateTable()
    }
}