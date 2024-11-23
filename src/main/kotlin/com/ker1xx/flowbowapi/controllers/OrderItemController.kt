package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.OrderItemEntity
import com.ker1xx.flowbowapi.services.OrderItemService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order-items")
class OrderItemController(private val orderItemService: OrderItemService) {

    // Получение всех элементов заказа
    @GetMapping
    fun getAllOrderItems(): List<OrderItemEntity> {
        return orderItemService.getAll()
    }

    // Получение элемента заказа по ID
    @GetMapping("/{id}")
    fun getOrderItemById(@PathVariable id: Int): OrderItemEntity {
        return orderItemService.getById(id)
            ?: throw NoSuchElementException("OrderItem with ID $id not found")
    }

    // Обновление элемента заказа
    @PutMapping("/{id}")
    fun updateOrderItem(
        @PathVariable id: Int,
        @RequestBody updatedOrderItem: OrderItemEntity
    ): OrderItemEntity {
        return orderItemService.update(id, updatedOrderItem)
    }

    // Создание нового элемента заказа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrderItem(@RequestBody orderItem: OrderItemEntity): OrderItemEntity {
        return orderItemService.save(orderItem)
    }

    // Удаление элемента заказа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrderItem(@PathVariable id: Int) {
        orderItemService.deleteById(id)
    }
}
