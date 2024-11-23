package com.ker1xx.flowbowapplication.models.dto

data class AuthorizationDataDTO(
    var id: Int?,
    val login: String,
    val password: String,
    val salt: String
)
