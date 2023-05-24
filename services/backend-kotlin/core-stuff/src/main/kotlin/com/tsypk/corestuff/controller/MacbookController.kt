package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.corestuff.controller.dto.macbook.MacbookUpdateRequest
import com.tsypk.corestuff.controller.dto.macbook.SupplierMacbookDto
import com.tsypk.corestuff.services.apple.macbook.MacBookService
import com.tsypk.corestuff.util.toSupplierMacbookDtoMapper
import com.tsypk.corestuff.util.toSupplierMacbookMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/macbooks")
class MacbookController(
    private val macBookService: MacBookService
) {
    @PostMapping(
        value = ["/update"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookSupplierUpdate(@RequestBody macbookUpdateRequest: MacbookUpdateRequest): ResponseEntity<Unit> {
        macBookService.updateAllForSupplier(macbookUpdateRequest.supplierId,
            macbookUpdateRequest.macbooks.map { it.toSupplierMacbookMapper(macbookUpdateRequest.supplierId) })
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(
        value = ["/find"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookSupplierFindBest(@RequestBody macbookFindBestRequest: MacbookFindBestRequest)
            : ResponseEntity<List<SupplierMacbookDto>> {
        val result = macBookService.getFindPrices(request = macbookFindBestRequest).map { it.toSupplierMacbookDtoMapper() }
        return ResponseEntity.ok().body(result)
    }

    @PostMapping(
        value = ["/truncate"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1MacbookSupplierTruncate(): ResponseEntity<Unit> {
        macBookService.truncateSuppliersIphones()
        return ResponseEntity.ok(Unit)
    }
}