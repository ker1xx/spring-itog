package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.BouquetEntity
import com.ker1xx.flowbowapi.services.BouquetService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bouquets")
class BouquetController(private val bouquetService: BouquetService) {

    // Получение всех букетов
    @GetMapping
    fun getAllBouquets(): List<BouquetEntity> {
        return bouquetService.getAll()
    }

    // Получение букета по ID
    @GetMapping("/{id}")
    fun getBouquetById(@PathVariable id: Int): BouquetEntity {
        return bouquetService.getById(id)
    }

    // Обновление букета по ID
    @PutMapping("/{id}")
    fun updateBouquet(
        @PathVariable id: Int,
        @RequestBody updatedBouquet: BouquetEntity
    ): BouquetEntity {
        return bouquetService.update(id, updatedBouquet)
    }

    // Создание нового букета
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBouquet(@RequestBody bouquet: BouquetEntity): BouquetEntity {
        return bouquetService.save(bouquet)
    }

    // Удаление букета по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBouquet(@PathVariable id: Int) {
        bouquetService.deleteById(id)
    }
}
