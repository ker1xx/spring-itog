package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "Clients")
data class ClientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClientId")
    val id: Int = 0,

    @OneToOne
    @JoinColumn(name = "AuthorizationDataId", nullable = false)
    val authorizationData: AuthorizationDataEntity,

    @OneToOne
    @JoinColumn(name = "PersonalDataId", nullable = false)
    val personalData: PersonalDataEntity
)
