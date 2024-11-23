package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "BouquetFlower")
@IdClass(BouquetFlowerKey::class)
data class BouquetFlowerEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "BouquetId", nullable = false)
    val bouquet: BouquetEntity,

    @Id
    @ManyToOne
    @JoinColumn(name = "FlowerId", nullable = false)
    val flower: FlowerEntity
)

data class BouquetFlowerKey(
    val bouquet: Int = 0,
    val flower: Int = 0
) : Serializable
