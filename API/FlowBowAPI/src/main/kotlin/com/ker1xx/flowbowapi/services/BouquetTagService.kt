package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.*
import com.ker1xx.flowbowapi.repository.BouquetTagRepository
import org.springframework.stereotype.Service

@Service
class BouquetTagService(
    private val bouquetTagRepository: BouquetTagRepository
) : BaseService<BouquetTagEntity, BouquetTagKey>(bouquetTagRepository) {

    override fun getEntityId(entity: BouquetTagEntity): BouquetTagKey {
        return BouquetTagKey(bouquet = entity.bouquet.id, tag = entity.tag.id) // Возвращаем ключ
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: BouquetTagEntity): BouquetTagEntity? {
        return bouquetTagRepository.findByBouquetIdAndTagId(entity.bouquet.id, entity.tag.id)
    }

    // Получение всех TagEntity по BouquetEntity
    fun getTagsByFlowerId(bouquetId: Int): List<TagEntity> {
        return bouquetTagRepository.findByBouquetId(bouquetId)
    }

    // Получение всех BouquetEntity по TagEntity
    fun getFlowersByTagId(tagId: Int): List<BouquetEntity> {
        return bouquetTagRepository.findByTagId(tagId)
    }

    fun update(id: BouquetTagKey, updatedBouquetTag: BouquetTagEntity): BouquetTagEntity {
        return super.update(id, updatedBouquetTag) { existing, updated ->
            existing.copy(
                bouquet = updated.bouquet,
                tag = updated.tag,
            )
        }
    }
}
