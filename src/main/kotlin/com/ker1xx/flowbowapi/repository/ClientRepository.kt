package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<ClientEntity, Int> {
    fun findByPersonalDataIdAndAuthorizationDataId(
        personalDataId: Int,
        authorizationDataId: Int
    ): ClientEntity?

    fun findByAuthorizationData(
        authorizationData: AuthorizationDataEntity
    ) : ClientEntity ?
}
