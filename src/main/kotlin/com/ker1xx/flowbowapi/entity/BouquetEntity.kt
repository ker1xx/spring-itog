package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Bouquets")
data class BouquetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BouquetId")
    val id: Int = 0,

    @Column(name = "BouquetName", nullable = false)
    val name: String,

    @Column(name = "Price", nullable = false)
    val price: Float,

    @Column(name = "Amount", nullable = false)
    val amount: Int,

    @Column(name = "IsAvailable")
    val isAvailable: Boolean? = null
)
