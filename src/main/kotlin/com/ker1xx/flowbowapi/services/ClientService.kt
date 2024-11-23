package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.ClientEntity
import com.ker1xx.flowbowapi.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientService(
    private val clientRepository: ClientRepository
) : BaseService<ClientEntity, Int>(clientRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: ClientEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: ClientEntity): ClientEntity? {
        return clientRepository.findByPersonalDataIdAndAuthorizationDataId(
            entity.personalData.id,
            entity.authorizationData.id
        )
    }

    fun update(id: Int, updatedClient: ClientEntity): ClientEntity {
        return super.update(id, updatedClient) { existing, updated ->
            existing.copy(
                authorizationData = updated.authorizationData,
                personalData = updated.personalData,
            )
        }
    }
//
//fun authorize()
}
