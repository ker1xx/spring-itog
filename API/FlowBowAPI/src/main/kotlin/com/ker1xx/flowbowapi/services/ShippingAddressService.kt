package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.ShippingAddressEntity
import com.ker1xx.flowbowapi.repository.ShippingAddressRepository
import org.springframework.stereotype.Service

@Service
class ShippingAddressService(
    private val shippingAddressRepository: ShippingAddressRepository
) : BaseService<ShippingAddressEntity, Int>(shippingAddressRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: ShippingAddressEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: ShippingAddressEntity): ShippingAddressEntity? {
        return shippingAddressRepository
            .findByClientAndStreetNameAndHouseNumberAndBuildingAndApartmentNumberAndCityAndRegionAndPostalCodeAndCountry(
            entity.client,
            entity.streetName,
            entity.houseNumber,
            entity.building,
            entity.apartmentNumber,
            entity.city,
            entity.region,
            entity.postalCode,
            entity.country
        )
    }

    fun update(id: Int, updatedShippingAddress: ShippingAddressEntity): ShippingAddressEntity {
        return super.update(id, updatedShippingAddress) { existing, updated ->
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
}
