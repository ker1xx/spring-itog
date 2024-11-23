package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.FlowerEntity
import com.ker1xx.flowbowapi.repository.FlowerRepository
import org.springframework.stereotype.Service

@Service
class FlowerService(
    private val flowerRepository: FlowerRepository
) : BaseService<FlowerEntity, Int>(flowerRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: FlowerEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: FlowerEntity): FlowerEntity? {
        return flowerRepository.findByName(entity.name)
    }

    fun update(id: Int, updatedFlower: FlowerEntity): FlowerEntity {
        return super.update(id, updatedFlower) { existing, updated ->
            existing.copy(
                name = updated.name,
                price = updated.price,
                amount = updated.amount,
                isAvailable = updated.isAvailable
            )
        }
    }
}
