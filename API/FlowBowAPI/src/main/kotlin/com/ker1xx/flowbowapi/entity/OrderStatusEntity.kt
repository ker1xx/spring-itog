package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "OrderStatuses")
data class OrderStatusEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderStatusId")
    val id: Int = 0,

    @Column(name = "OrderStatusName", nullable = false)
    val name: String
)

