package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<ClientEntity, Int> {
    fun findByPersonalDataAndAuthorizationData(
        personalDataId: Int,
        authorizationDataId: Int
    ): ClientEntity
}
