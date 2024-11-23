package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.BouquetFlowerEntity
import com.ker1xx.flowbowapi.entity.BouquetFlowerKey
import com.ker1xx.flowbowapi.repository.BouquetFlowerRepository
import org.springframework.stereotype.Service

@Service
class BouquetFlowerService(
    private val bouquetFlowerRepository: BouquetFlowerRepository
) : BaseService<BouquetFlowerEntity, BouquetFlowerKey>(bouquetFlowerRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: BouquetFlowerEntity): BouquetFlowerKey {
        return BouquetFlowerKey(bouquet = entity.bouquet.id, flower = entity.flower.id) // Возвращаем ключ
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: BouquetFlowerEntity): BouquetFlowerEntity? {
        return bouquetFlowerRepository.findByBouquetIdAndFlowerId(entity.bouquet.id, entity.flower.id)
    }

    // Получение всех FlowerEntity по BouquetEntity
    fun getFlowersByBouquetId(bouquetId: Int): List<BouquetFlowerEntity> {
        return bouquetFlowerRepository.findByBouquetId(bouquetId)
    }

    // Получение всех BouquetEntity по FlowerEntity
    fun getBouquetsByFlowerId(flowerId: Int): List<BouquetFlowerEntity> {
        return bouquetFlowerRepository.findByFlowerId(flowerId)
    }

    fun update(id: BouquetFlowerKey, updatedBouquetFlower: BouquetFlowerEntity): BouquetFlowerEntity {
        return super.update(id, updatedBouquetFlower) { existing, updated ->
            existing.copy(
                bouquet = updated.bouquet,
                flower = updated.flower
            )
        }
    }
}
