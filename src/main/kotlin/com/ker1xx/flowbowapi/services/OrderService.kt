package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.OrderEntity
import com.ker1xx.flowbowapi.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository
) : BaseService<OrderEntity, Int>(orderRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: OrderEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: OrderEntity): OrderEntity? {
        return orderRepository.findByCreationDateAndDateAndPriceAndCommentAndClientAndAddressAndStatus(
            entity.creationDate,
            entity.date,
            entity.price,
            entity.comment,
            entity.client,
            entity.address,
            entity.status)
    }

    fun update(id: Int, updatedOrder: OrderEntity): OrderEntity {
        return super.update(id, updatedOrder) { existing, updated ->
            existing.copy(
                creationDate = updated.creationDate,
                date = updated.date,
                price = updated.price,
                comment = updated.comment,
                client = updated.client,
                address = updated.address,
                status = updated.status
            )
        }
    }
}
