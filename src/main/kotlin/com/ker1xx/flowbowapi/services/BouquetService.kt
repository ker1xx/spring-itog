package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.BouquetEntity
import com.ker1xx.flowbowapi.repository.BouquetRepository
import org.springframework.stereotype.Service

@Service
class BouquetService(
    private val bouquetRepository: BouquetRepository
) : BaseService<BouquetEntity, Int>(bouquetRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: BouquetEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: BouquetEntity): BouquetEntity? {
        return bouquetRepository.findByName(entity.name)
    }

    fun update(id: Int, updatedBouquet: BouquetEntity): BouquetEntity {
        return super.update(id, updatedBouquet) { existing, updated ->
            existing.copy(
                name = updated.name,
                price = updated.price,
                amount = updated.amount,
                isAvailable = updated.isAvailable
            )
        }
    }
}
