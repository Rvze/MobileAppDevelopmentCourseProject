package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.UserDto
import com.tsypk.corestuff.exception.UserNotFoundException
import com.tsypk.corestuff.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/user")
class UserController(
    private val userRepository: UserRepository,
) {
    @GetMapping(value = ["get"])
    fun getSupplierStuffFromId(@RequestHeader("UserID") userId: Long): ResponseEntity<UserDto> {
        val user = userRepository.get(userId)
            ?: throw UserNotFoundException(userId)
        return ResponseEntity.ok(user.toDto())
    }
}
