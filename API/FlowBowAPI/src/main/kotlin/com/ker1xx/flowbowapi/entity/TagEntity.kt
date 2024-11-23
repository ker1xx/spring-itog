package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Tags")
data class TagEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagId")
    val id: Int = 0,

    @Column(name = "TagName", nullable = false)
    val name: String
)
