package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "BouquetTag")
@IdClass(BouquetTagKey::class)
data class BouquetTagEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "BouquetId", nullable = false)
    val bouquet: BouquetEntity,

    @Id
    @ManyToOne
    @JoinColumn(name = "TagId", nullable = false)
    val tag: TagEntity
)

data class BouquetTagKey(
    val bouquet: Int = 0,
    val tag: Int = 0
) : Serializable
