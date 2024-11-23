package com.ker1xx.flowbowapplication.models.dto

data class BouquetDTO(
    val id: Int?,
    val name: String,
    val price: Float,
    val amount: Int,
    val isAvailable: Boolean?
)
