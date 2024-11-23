package com.ker1xx.flowbowapi.services.helpers

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.repository.AdministratorRepository
import com.ker1xx.flowbowapi.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val clientRepository: ClientRepository,
    private val administratorRepository: AdministratorRepository
) {
    fun getRole(authorizationData: AuthorizationDataEntity): String? {
        val clientEntity = clientRepository.findByAuthorizationData(authorizationData)
        val adminEntity = administratorRepository.findByAuthorizationData(authorizationData)
        return when {
            clientEntity != null -> "client"
            adminEntity != null -> "administrator"
            else -> null
        }
    }
}