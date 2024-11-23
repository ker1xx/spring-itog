package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "AuthorizationData")
data class AuthorizationDataEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuthorizationDataId")
    val id: Int = 0,

    @Column(name = "Login", nullable = false)
    val login: String,

    @Column(name = "Password", nullable = false)
    val password: String,

    @Column(name = "Salt", nullable = false)
    val salt: String
)

