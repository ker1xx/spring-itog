package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Administrators")
data class AdministratorEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdministratorId")
    val id: Int = 0,

    @Column(name = "Name", nullable = false)
    val name: String,

    @Column(name = "Lastname", nullable = false)
    val lastname: String,

    @Column(name = "Patronymic")
    val patronymic: String? = null,

    @OneToOne
    @JoinColumn(name = "AuthorizationDataId")
    val authorizationData: AuthorizationDataEntity? = null
)
