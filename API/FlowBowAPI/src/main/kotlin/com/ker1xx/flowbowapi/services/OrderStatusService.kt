package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.OrderStatusEntity
import com.ker1xx.flowbowapi.repository.OrderStatusRepository
import org.springframework.stereotype.Service

@Service
class OrderStatusService(
    private val orderStatusRepository: OrderStatusRepository
) : BaseService<OrderStatusEntity, Int>(orderStatusRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: OrderStatusEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: OrderStatusEntity): OrderStatusEntity? {
        return orderStatusRepository.findByName(entity.name)
    }

    fun update(id: Int, updatedStatusOrder: OrderStatusEntity): OrderStatusEntity {
        return super.update(id, updatedStatusOrder) { existing, updated ->
            existing.copy(
                name = updated.name
            )
        }
    }
}
