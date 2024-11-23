package com.ker1xx.flowbowapplication.models.dto

data class ClientDTO (
    val id: Int?,
    val authorizationData: AuthorizationDataDTO,
    val personalData: PersonalDataDTO
)