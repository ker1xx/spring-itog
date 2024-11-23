package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.OrderItemEntity
import com.ker1xx.flowbowapi.repository.OrderItemRepository
import org.springframework.stereotype.Service

@Service
class OrderItemService(
    private val orderItemRepository: OrderItemRepository
) : BaseService<OrderItemEntity, Int>(orderItemRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: OrderItemEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: OrderItemEntity): OrderItemEntity? {
        return orderItemRepository.findByOrderIdAndFlowerIdAndBouquetIdAndQuantity(
            entity.order.id,
            entity.flower?.id,
            entity.bouquet?.id,
            entity.quantity
        )
    }

    fun update(id: Int, updatedOrderItem: OrderItemEntity): OrderItemEntity {
        return super.update(id, updatedOrderItem) { existing, updated ->
            existing.copy(
                quantity = updated.quantity,
                flower = updated.flower,
                bouquet = updated.bouquet,
                order = updated.order
            )
        }
    }
}
