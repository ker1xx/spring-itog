package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.PersonalDataEntity
import com.ker1xx.flowbowapi.repository.PersonalDataRepository
import org.springframework.stereotype.Service

@Service
class PersonalDataService(
    private val personalDataRepository: PersonalDataRepository
) : BaseService<PersonalDataEntity, Int>(personalDataRepository) {

    // Переопределяем метод для получения ID сущности
    override fun getEntityId(entity: PersonalDataEntity): Int {
        return entity.id
    }

    // Переопределяем метод для поиска дубликатов
    override fun findDuplicateEntity(entity: PersonalDataEntity): PersonalDataEntity? {
        return personalDataRepository.findByNameAndLastnameAndPatronymicAndPhoneNumber(
            entity.name,
            entity.lastname,
            entity.patronymic,
            entity.phoneNumber
        )
    }

    fun update(id: Int, updatedPersonalData: PersonalDataEntity): PersonalDataEntity {
        return super.update(id, updatedPersonalData) { existing, updated ->
            existing.copy(
                name = updated.name,
                lastname = updated.lastname,
                patronymic = updated.patronymic,
                phoneNumber = updated.phoneNumber
            )
        }
    }
}
