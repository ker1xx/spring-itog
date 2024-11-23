package com.ker1xx.flowbowapplication.models.dto


data class AdministratorDTO(
    val id: Int,
    val name: String,
    val lastname: String,
    val patronymic: String?,
    val authorizationData: AuthorizationDataDTO
)
