package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.ShippingAddressEntity
import com.ker1xx.flowbowapi.services.ShippingAddressService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/shipping-addresses")
class ShippingAddressController(private val shippingAddressService: ShippingAddressService) {

    // Получение всех адресов доставки
    @GetMapping
    fun getAllShippingAddresses(): List<ShippingAddressEntity> {
        return shippingAddressService.getAll()
    }

    // Получение адреса доставки по ID
    @GetMapping("/{id}")
    fun getShippingAddressById(@PathVariable id: Int): ShippingAddressEntity {
        return shippingAddressService.getById(id)
    }

    // Обновление адреса доставки
    @PutMapping("/{id}")
    fun updateShippingAddress(
        @PathVariable id: Int,
        @RequestBody updatedShippingAddress: ShippingAddressEntity
    ): ShippingAddressEntity {
        return shippingAddressService.update(id, updatedShippingAddress) { existing, updated ->
            existing.copy(
                client = updated.client,
                streetName = updated.streetName,
                houseNumber = updated.houseNumber,
                building = updated.building,
                apartmentNumber = updated.apartmentNumber,
                city = updated.city,
                region = updated.region,
                postalCode = updated.postalCode,
                country = updated.country,
                salt = updated.salt
            )
        }
    }

    // Создание нового адреса доставки
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createShippingAddress(@RequestBody shippingAddress: ShippingAddressEntity): ShippingAddressEntity {
        return shippingAddressService.save(shippingAddress)
    }

    // Удаление адреса доставки
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteShippingAddress(@PathVariable id: Int) {
        shippingAddressService.deleteById(id)
    }
}
