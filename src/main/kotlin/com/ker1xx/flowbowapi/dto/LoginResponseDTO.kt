package com.ker1xx.flowbowapi.dto

data class LoginResponseDTO(
    val token: String,
    val role: String,
    val id: Int
)