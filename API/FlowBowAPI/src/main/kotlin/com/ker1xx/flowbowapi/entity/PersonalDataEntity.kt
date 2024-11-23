package com.ker1xx.flowbowapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "PersonalData")
data class PersonalDataEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonalDataId")
    val id: Int = 0,

    @Column(name = "Name", nullable = false)
    val name: String,

    @Column(name = "Lastname", nullable = false)
    val lastname: String,

    @Column(name = "Patronymic")
    val patronymic: String? = null,

    @Column(name = "PhoneNumber", nullable = false)
    val phoneNumber: String,

    @Column(name = "Salt", nullable = false)
    val salt: String
)
