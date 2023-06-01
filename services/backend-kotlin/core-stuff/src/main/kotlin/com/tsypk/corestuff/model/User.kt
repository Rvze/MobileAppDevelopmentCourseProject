package com.tsypk.corestuff.model

import com.tsypk.corestuff.controller.dto.UserDto

data class User(
    val id: Long,
    val username: String,
    val role: String,
) {
    fun toDto(): UserDto = UserDto(id, username, role)
}