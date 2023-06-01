package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import com.tsypk.corestuff.controller.dto.stuff.request.TextRequest
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.services.StuffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/stuff")
class StuffController(
    private val stuffService: StuffService
) {

    @PostMapping(
        value = ["search-by-text"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun searchByText(@RequestBody request: TextRequest): ResponseEntity<List<StuffSearchResponse>> {
        val response = stuffService.searchByText(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping(
        value = ["buy-request"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun buyStuff(
        @RequestHeader("UserID") userId: Long,
        @RequestBody buyStuffRequest: BuyStuffRequest
    ): ResponseEntity<Unit> {
        stuffService.buyStuff(buyStuffRequest, userId)
        return ResponseEntity.ok(Unit)
    }
}
