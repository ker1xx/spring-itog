package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "FlowerTag")
@IdClass(FlowerTagKey::class)
data class FlowerTagEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "FlowerId", nullable = false)
    val flower: FlowerEntity,

    @Id
    @ManyToOne
    @JoinColumn(name = "TagId", nullable = false)
    val tag: TagEntity
)

data class FlowerTagKey(
    val flower: Int = 0,
    val tag: Int = 0
) : Serializable
