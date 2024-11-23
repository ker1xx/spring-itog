package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.ClientEntity
import com.ker1xx.flowbowapi.entity.OrderEntity
import com.ker1xx.flowbowapi.entity.OrderStatusEntity
import com.ker1xx.flowbowapi.entity.ShippingAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.Date

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Int>{
    fun findByCreationDateAndDateAndPriceAndCommentAndClientAndAddressAndStatus(
        creationDate: Date,
        date: Date,
        price: Float,
        comment: String?,
        client: ClientEntity,
        address: ShippingAddressEntity,
        status: OrderStatusEntity
    ) : OrderEntity?
}
