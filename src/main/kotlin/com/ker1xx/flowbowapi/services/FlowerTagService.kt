package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.*
import com.ker1xx.flowbowapi.repository.FlowerTagRepository
import org.springframework.stereotype.Service

@Service
class TagFlowerService(
    private val flowerTagRepository: FlowerTagRepository
) : BaseService<FlowerTagEntity, FlowerTagKey>(flowerTagRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: FlowerTagEntity): FlowerTagKey {
        return FlowerTagKey(tag = entity.tag.id, flower = entity.flower.id) // Возвращаем ключ
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: FlowerTagEntity): FlowerTagEntity? {
        return flowerTagRepository.findByFlowerIdAndTagId(entity.tag.id, entity.flower.id)
    }

    // Получение всех TagEntity по FlowerEntity
    fun getTagsByFlowerId(flowerId: Int): List<FlowerTagEntity>? {
        return flowerTagRepository.findByFlowerId(flowerId)
    }

    // Получение всех FlowerEntity по TagEntity
    fun getFlowersByTagId(tagId: Int): List<FlowerTagEntity>? {
        return flowerTagRepository.findByTagId(tagId)
    }

    fun update(id: FlowerTagKey, updatedFlowerTag: FlowerTagEntity): FlowerTagEntity {
        return super.update(id, updatedFlowerTag) { existing, updated ->
            existing.copy(
                flower = updated.flower,
                tag = updated.tag
            )
        }
    }
}
