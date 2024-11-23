package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.OrderEntity
import com.ker1xx.flowbowapi.services.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {

    // Получение всех заказов
    @GetMapping
    fun getAllOrders(): List<OrderEntity> {
        return orderService.getAll()
    }

    // Получение заказа по ID
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): OrderEntity {
        return orderService.getById(id)
    }

    // Обновление заказа
    @PutMapping("/{id}")
    fun updateOrder(
        @PathVariable id: Int,
        @RequestBody updatedOrder: OrderEntity
    ): OrderEntity {
        return orderService.update(id, updatedOrder) { existing, updated ->
            existing.copy(
                creationDate = updated.creationDate,
                date = updated.date,
                price = updated.price,
                comment = updated.comment,
                client = updated.client,
                address = updated.address,
                status = updated.status
            )
        }
    }

    // Создание нового заказа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody order: OrderEntity): OrderEntity {
        return orderService.save(order)
    }

    // Удаление заказа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable id: Int) {
        orderService.deleteById(id)
    }
}
