package com.ker1xx.flowbowapplication.models.dto

data class RegistrationResponseDTO(
    val id: Int, // id созданного объекта
    val token: String  // JWT токен
)
