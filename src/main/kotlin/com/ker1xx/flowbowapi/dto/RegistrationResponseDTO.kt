package com.ker1xx.flowbowapi.dto

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity

data class RegistrationResponseDTO(
    val id: Int, // id созданного объекта
    val token: String  // JWT токен
)
