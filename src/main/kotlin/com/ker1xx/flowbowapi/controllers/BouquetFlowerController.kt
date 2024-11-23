package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.BouquetFlowerEntity
import com.ker1xx.flowbowapi.entity.BouquetFlowerKey
import com.ker1xx.flowbowapi.services.BouquetFlowerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bouquets-flowers")
class BouquetFlowerController(private val bouquetFlowerService: BouquetFlowerService) {

    // Получение всех цветов для букета по ID букета
    @GetMapping("/bouquet/{bouquetId}")
    fun getFlowersByBouquet(@PathVariable bouquetId: Int): List<BouquetFlowerEntity>? {
        return bouquetFlowerService.getFlowersByBouquetId(bouquetId)
    }

    // Получение всех букетов для цветка по ID цветка
    @GetMapping("/flower/{flowerId}")
    fun getBouquetsByFlower(@PathVariable flowerId: Int): List<BouquetFlowerEntity>? {
        return bouquetFlowerService.getBouquetsByFlowerId(flowerId)
    }

    // Обновление данных о букете и цветке по их ID
    @PutMapping("/{bouquetId}/{flowerId}")
    fun updateBouquetFlower(
        @PathVariable bouquetId: Int,
        @PathVariable flowerId: Int,
        @RequestBody updatedBouquetFlower: BouquetFlowerEntity
    ): BouquetFlowerEntity {
        val bouquetFlowerKey = BouquetFlowerKey(bouquetId, flowerId)
        return bouquetFlowerService.update(bouquetFlowerKey, updatedBouquetFlower)
    }

    // Создание новой записи для букета и цветка
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBouquetFlower(@RequestBody bouquetFlower: BouquetFlowerEntity): BouquetFlowerEntity {
        return bouquetFlowerService.save(bouquetFlower)
    }

    // Удаление записи о букете и цветке по ID
    @DeleteMapping("/{bouquetId}/{flowerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBouquetFlower(
        @PathVariable bouquetId: Int,
        @PathVariable flowerId: Int
    ) {
        val bouquetFlowerKey = BouquetFlowerKey(bouquetId, flowerId)
        bouquetFlowerService.deleteById(bouquetFlowerKey)
    }
}
