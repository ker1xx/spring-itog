package com.ker1xx.flowbowapplication.models.customEntities

import com.ker1xx.flowbowapplication.models.dto.FlowerDTO
import com.ker1xx.flowbowapplication.models.dto.TagDTO

data class BouquetWithTagDisplayEntity(
    val id: Int?,
    val name: String,
    val price: Float,
    val amount: Int,
    val isAvailable: Boolean?,
    @Transient var tags: List<TagDTO> = emptyList(),
    @Transient var flowers: List<FlowerDTO> = emptyList()
)