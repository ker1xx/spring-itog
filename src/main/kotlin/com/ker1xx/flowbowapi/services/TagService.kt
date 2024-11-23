package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.TagEntity
import com.ker1xx.flowbowapi.repository.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) : BaseService<TagEntity, Int>(tagRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: TagEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: TagEntity): TagEntity? {
        return tagRepository.findByName(entity.name)
    }

    fun update(id: Int, updatedTag: TagEntity): TagEntity {
        return super.update(id, updatedTag) { existing, updated ->
            existing.copy(
                name = updated.name
            )
        }
    }
}
