package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.response.StuffSearchResponse
import com.tsypk.corestuff.services.StuffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/stuff")
class StuffController(
    private val stuffService: StuffService
) {

    @PostMapping(value = ["search-by-text"])
    fun v1SearchByText(@RequestBody text: String): ResponseEntity<List<StuffSearchResponse>> {
        val response = stuffService.searchByText(text)
        return ResponseEntity.ok(response)
    }
}