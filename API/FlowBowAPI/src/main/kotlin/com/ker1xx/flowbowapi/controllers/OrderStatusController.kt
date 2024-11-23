package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.OrderStatusEntity
import com.ker1xx.flowbowapi.services.OrderStatusService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order-statuses")
class OrderStatusController(private val orderStatusService: OrderStatusService) {

    // Получение всех статусов заказов
    @GetMapping
    fun getAllOrderStatuses(): List<OrderStatusEntity> {
        return orderStatusService.getAll()
    }

    // Получение статуса заказа по ID
    @GetMapping("/{id}")
    fun getOrderStatusById(@PathVariable id: Int): OrderStatusEntity {
        return orderStatusService.getById(id)
    }

    // Обновление статуса заказа
    @PutMapping("/{id}")
    fun updateOrderStatus(
        @PathVariable id: Int,
        @RequestBody updatedStatusOrder: OrderStatusEntity
    ): OrderStatusEntity {
        return orderStatusService.update(id, updatedStatusOrder) { existing, updated ->
            existing.copy(
                name = updated.name
            )
        }
    }

    // Создание нового статуса заказа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrderStatus(@RequestBody orderStatus: OrderStatusEntity): OrderStatusEntity {
        return orderStatusService.save(orderStatus)
    }

    // Удаление статуса заказа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrderStatus(@PathVariable id: Int) {
        orderStatusService.deleteById(id)
    }
}
