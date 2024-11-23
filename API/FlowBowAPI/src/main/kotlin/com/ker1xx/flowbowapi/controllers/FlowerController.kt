package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.FlowerEntity
import com.ker1xx.flowbowapi.services.FlowerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/flowers")
class FlowerController(private val flowerService: FlowerService) {

    // Получение всех цветов
    @GetMapping
    fun getAllFlowers(): List<FlowerEntity> {
        return flowerService.getAll()
    }

    // Получение цветка по ID
    @GetMapping("/{id}")
    fun getFlowerById(@PathVariable id: Int): FlowerEntity {
        return flowerService.getById(id)
    }

    // Обновление информации о цветке
    @PutMapping("/{id}")
    fun updateFlower(
        @PathVariable id: Int,
        @RequestBody updatedFlower: FlowerEntity
    ): FlowerEntity {
        return flowerService.update(id, updatedFlower)
    }

    // Создание нового цветка
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFlower(@RequestBody flower: FlowerEntity): FlowerEntity {
        return flowerService.save(flower)
    }

    // Удаление цветка по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFlower(@PathVariable id: Int) {
        flowerService.deleteById(id)
    }
}
