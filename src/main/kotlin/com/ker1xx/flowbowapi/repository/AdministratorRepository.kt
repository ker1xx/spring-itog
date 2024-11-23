package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.AdministratorEntity
import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdministratorRepository : JpaRepository<AdministratorEntity, Int> {

    fun findByNameAndLastnameAndPatronymicAndAuthorizationDataId(
        name: String,
        lastname: String,
        patronymic: String?,
        authorizationId: Int?
    ): AdministratorEntity?

    fun findByAuthorizationData(
        authorizationData: AuthorizationDataEntity
    ) : AdministratorEntity?

}
