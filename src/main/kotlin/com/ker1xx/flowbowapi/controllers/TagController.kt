package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.TagEntity
import com.ker1xx.flowbowapi.services.TagService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagController(private val tagService: TagService) {

    // Получение всех тегов
    @GetMapping
    fun getAllTags(): List<TagEntity> {
        return tagService.getAll()
    }

    // Получение тега по ID
    @GetMapping("/{id}")
    fun getTagById(@PathVariable id: Int): TagEntity {
        return tagService.getById(id)
    }

    // Обновление тега
    @PutMapping("/{id}")
    fun updateTag(
        @PathVariable id: Int,
        @RequestBody updatedTag: TagEntity
    ): TagEntity {
        return tagService.update(id, updatedTag) { existing, updated ->
            existing.copy(
                name = updated.name
            )
        }
    }

    // Создание нового тега
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTag(@RequestBody tag: TagEntity): TagEntity {
        return tagService.save(tag)
    }

    // Удаление тега
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTag(@PathVariable id: Int) {
        tagService.deleteById(id)
    }
}
