package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.PersonalDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonalDataRepository : JpaRepository<PersonalDataEntity, Int>{
    fun findByNameAndLastnameAndPatronymicAndPhoneNumber(
        name: String,
        lastname: String,
        patronymic: String?,
        phoneNumber: String
    ) : PersonalDataEntity?
}
