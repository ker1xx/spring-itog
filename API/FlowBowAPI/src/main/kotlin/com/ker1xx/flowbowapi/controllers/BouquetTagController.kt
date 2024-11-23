package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.BouquetEntity
import com.ker1xx.flowbowapi.entity.BouquetTagEntity
import com.ker1xx.flowbowapi.entity.BouquetTagKey
import com.ker1xx.flowbowapi.entity.TagEntity
import com.ker1xx.flowbowapi.services.BouquetTagService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bouquet-tags")
class BouquetTagController(private val bouquetTagService: BouquetTagService) {

    // Получение всех записей BouquetTag
    @GetMapping
    fun getAllBouquetTags(): List<BouquetTagEntity> {
        return bouquetTagService.getAll()
    }

    // Получение записи BouquetTag по ID
    @GetMapping("/{bouquetId}/{tagId}")
    fun getBouquetTagById(
        @PathVariable bouquetId: Int,
        @PathVariable tagId: Int
    ): BouquetTagEntity {
        val key = BouquetTagKey(bouquet = bouquetId, tag = tagId)
        return bouquetTagService.getById(key)
    }

    // Обновление записи BouquetTag
    @PutMapping("/{bouquetId}/{tagId}")
    fun updateBouquetTag(
        @PathVariable bouquetId: Int,
        @PathVariable tagId: Int,
        @RequestBody updatedBouquetTag: BouquetTagEntity
    ): BouquetTagEntity {
        val key = BouquetTagKey(bouquet = bouquetId, tag = tagId)
        return bouquetTagService.update(key, updatedBouquetTag)
    }

    // Создание новой записи BouquetTag
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBouquetTag(@RequestBody bouquetTag: BouquetTagEntity): BouquetTagEntity {
        return bouquetTagService.save(bouquetTag)
    }

    // Удаление записи BouquetTag по ID
    @DeleteMapping("/{bouquetId}/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBouquetTag(
        @PathVariable bouquetId: Int,
        @PathVariable tagId: Int
    ) {
        val key = BouquetTagKey(bouquet = bouquetId, tag = tagId)
        bouquetTagService.deleteById(key)
    }

    // Получение всех TagEntity по BouquetId
    @GetMapping("/bouquet/{bouquetId}")
    fun getTagsByBouquetId(@PathVariable bouquetId: Int): List<TagEntity> {
        return bouquetTagService.getTagsByFlowerId(bouquetId)
    }

    // Получение всех BouquetEntity по TagId
    @GetMapping("/tag/{tagId}")
    fun getBouquetsByTagId(@PathVariable tagId: Int): List<BouquetEntity> {
        return bouquetTagService.getFlowersByTagId(tagId)
    }
}
