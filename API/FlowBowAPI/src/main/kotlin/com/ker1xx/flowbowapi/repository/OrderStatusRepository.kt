package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.OrderStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderStatusRepository : JpaRepository<OrderStatusEntity, Int>{
    fun findByName(name: String): OrderStatusEntity
}
