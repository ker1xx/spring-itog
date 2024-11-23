package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.repository.AuthorizationDataRepository
import org.springframework.stereotype.Service

@Service
class AuthorizationDataService(
    private val authorizationDataRepository: AuthorizationDataRepository
) : BaseService<AuthorizationDataEntity, Int>(authorizationDataRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: AuthorizationDataEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: AuthorizationDataEntity): AuthorizationDataEntity? {
        return authorizationDataRepository.findByLogin(entity.login)
    }

    fun update(id: Int, updatedAuthorizationData: AuthorizationDataEntity): AuthorizationDataEntity {
        return super.update(id, updatedAuthorizationData) { existing, updated ->
            existing.copy(
                login = updated.login,
                password = updated.password,
                salt = updated.salt
            )
        }
    }
}
