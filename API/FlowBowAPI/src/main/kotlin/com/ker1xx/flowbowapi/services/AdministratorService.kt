package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.AdministratorEntity
import com.ker1xx.flowbowapi.repository.AdministratorRepository
import org.springframework.stereotype.Service

@Service
class AdministratorService(private val administratorRepository: AdministratorRepository) :
    BaseService<AdministratorEntity, Int>(administratorRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: AdministratorEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: AdministratorEntity): AdministratorEntity? {
        return administratorRepository.findByNameAndLastnameAndPatronymic(
            entity.name, entity.lastname, entity.patronymic, entity.authorizationData?.id
        )
    }

    fun update(id: Int, updatedAdministrator: AdministratorEntity): AdministratorEntity {
        return super.update(id, updatedAdministrator) { existing, updated ->
            existing.copy(
                name = updated.name,
                lastname = updated.lastname,
                patronymic = updated.patronymic,
                authorizationData = updated.authorizationData
            )
        }
    }

}
