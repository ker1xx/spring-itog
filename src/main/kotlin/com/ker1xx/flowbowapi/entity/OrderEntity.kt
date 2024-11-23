package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderId")
    val id: Int = 0,

    @Column(name = "CreationDate", nullable = false)
    val creationDate: java.sql.Date,

    @Column(name = "Date", nullable = false)
    val date: java.sql.Date,

    @Column(name = "Price", nullable = false)
    val price: Float,

    @Column(name = "Comment")
    val comment: String? = null,

    @ManyToOne
    @JoinColumn(name = "ClientId", nullable = false)
    val client: ClientEntity,

    @ManyToOne
    @JoinColumn(name = "AddressId", nullable = false)
    val address: ShippingAddressEntity,

    @ManyToOne
    @JoinColumn(name = "StatusId", nullable = false)
    val status: OrderStatusEntity
)

