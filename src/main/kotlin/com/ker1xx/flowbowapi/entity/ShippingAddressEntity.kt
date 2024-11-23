package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "ShippingAddresses")
data class ShippingAddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShippingAddressId")
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "ClientId", nullable = false)
    val client: ClientEntity,

    @Column(name = "StreetName", nullable = false)
    val streetName: String,

    @Column(name = "HouseNumber", nullable = false)
    val houseNumber: String,

    @Column(name = "Building")
    val building: String? = null,

    @Column(name = "ApartmentNumber")
    val apartmentNumber: String? = null,

    @Column(name = "City", nullable = false)
    val city: String,

    @Column(name = "Region")
    val region: String? = null,

    @Column(name = "Postal_code", nullable = false)
    val postalCode: String,

    @Column(name = "Country", nullable = false)
    val country: String,
)

