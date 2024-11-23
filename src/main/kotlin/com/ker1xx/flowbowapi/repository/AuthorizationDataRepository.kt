package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorizationDataRepository : JpaRepository<AuthorizationDataEntity, Int>{

    fun findByLogin(loginConfig: String): AuthorizationDataEntity?
}
