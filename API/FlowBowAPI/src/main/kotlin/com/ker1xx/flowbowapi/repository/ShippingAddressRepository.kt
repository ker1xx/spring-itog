package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.ClientEntity
import com.ker1xx.flowbowapi.entity.ShippingAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShippingAddressRepository : JpaRepository<ShippingAddressEntity, Int>{
    fun findByClientAndStreetNameAndHouseNumberAndBuildingAndApartmentNumberAndCityAndRegionAndPostalCodeAndCountry(
        client: ClientEntity,
        streetName: String,
        houseNumber: String,
        building: String?,
        apartmentNumber: String?,
        city: String,
        region: String?,
        postalCode: String,
        country: String
    ) : ShippingAddressEntity
}
