package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository : JpaRepository<OrderItemEntity, Int> {
    fun findByOrderIdAndFlowerIdAndBouquetIdAndQuantity(
        orderId: Int,
        flowerId: Int?,
        bouquetId: Int?,
        quantity: Int
    ): OrderItemEntity?
}
