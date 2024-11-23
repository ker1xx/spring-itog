package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.FlowerEntity
import com.ker1xx.flowbowapi.entity.FlowerTagEntity
import com.ker1xx.flowbowapi.entity.FlowerTagKey
import com.ker1xx.flowbowapi.entity.TagEntity
import com.ker1xx.flowbowapi.services.TagFlowerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/flowers-tags")
class TagFlowerController(private val tagFlowerService: TagFlowerService) {

    // Получение всех тегов для цветка
    @GetMapping("/flower/{flowerId}")
    fun getTagsByFlowerId(@PathVariable flowerId: Int): List<FlowerTagEntity>? {
        return tagFlowerService.getTagsByFlowerId(flowerId)
    }

    // Получение всех цветов для тега
    @GetMapping("/tag/{tagId}")
    fun getFlowersByTagId(@PathVariable tagId: Int): List<FlowerTagEntity>? {
        return tagFlowerService.getFlowersByTagId(tagId)
    }

    // Обновление связи цветка и тега
    @PutMapping("/{flowerId}/{tagId}")
    fun updateFlowerTag(
        @PathVariable flowerId: Int,
        @PathVariable tagId: Int,
        @RequestBody updatedFlowerTag: FlowerTagEntity
    ): FlowerTagEntity {
        val key = FlowerTagKey(flower = flowerId, tag = tagId)
        return tagFlowerService.update(key, updatedFlowerTag)
    }

    // Создание новой связи цветка и тега
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFlowerTag(@RequestBody flowerTag: FlowerTagEntity): FlowerTagEntity {
        return tagFlowerService.save(flowerTag)
    }

    // Удаление связи цветка и тега
    @DeleteMapping("/{flowerId}/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFlowerTag(@PathVariable flowerId: Int, @PathVariable tagId: Int) {
        val key = FlowerTagKey(flower = flowerId, tag = tagId)
        tagFlowerService.deleteById(key)
    }
}
