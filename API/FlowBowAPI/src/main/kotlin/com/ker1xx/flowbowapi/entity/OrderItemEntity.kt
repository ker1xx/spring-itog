package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "OrderItems")
data class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemId")
    val id: Int = 0,

    @Column(name = "Quantity", nullable = false)
    val quantity: Int,

    @ManyToOne
    @JoinColumn(name = "FlowerId")
    val flower: FlowerEntity? = null,

    @ManyToOne
    @JoinColumn(name = "BouquetId")
    val bouquet: BouquetEntity? = null,

    @ManyToOne
    @JoinColumn(name = "OrderId", nullable = false)
    val order: OrderEntity
)
