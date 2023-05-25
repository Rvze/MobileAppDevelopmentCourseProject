package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.services.SupplierService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/supplier")
class SupplierController(
    private val supplierService: SupplierService
) {

    @GetMapping(value = ["stuff"])
    fun getSupplierStuffFromId(@RequestHeader userId: Long): ResponseEntity<List<StuffSearchResponse>> {
        val response = supplierService.getSupplierStuffById(userId)
        return ResponseEntity.ok(response)
    }

}