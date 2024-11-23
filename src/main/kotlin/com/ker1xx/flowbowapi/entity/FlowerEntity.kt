package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Flowers")
data class FlowerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FlowerId")
    val id: Int = 0,

    @Column(name = "FlowerName", nullable = false)
    val name: String,

    @Column(name = "Price", nullable = false)
    val price: Float,

    @Column(name = "Amount", nullable = false)
    val amount: Int,

    @Column(name = "IsAvailable", nullable = false)
    val isAvailable: Boolean
)
