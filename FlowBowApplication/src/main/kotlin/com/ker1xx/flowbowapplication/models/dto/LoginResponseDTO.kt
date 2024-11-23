package com.ker1xx.flowbowapplication.models.dto

data class LoginResponseDTO(
    val token: String,
    val role: String,
    val id: Int
)