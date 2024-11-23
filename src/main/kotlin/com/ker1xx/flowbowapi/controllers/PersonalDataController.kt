package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.PersonalDataEntity
import com.ker1xx.flowbowapi.services.PersonalDataService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/personal-data")
class PersonalDataController(private val personalDataService: PersonalDataService) {

    // Получение всех персональных данных
    @GetMapping
    fun getAllPersonalData(): List<PersonalDataEntity> {
        return personalDataService.getAll()
    }

    // Получение персональных данных по ID
    @GetMapping("/{id}")
    fun getPersonalDataById(@PathVariable id: Int): PersonalDataEntity {
        return personalDataService.getById(id)
    }

    // Обновление персональных данных
    @PutMapping("/{id}")
    fun updatePersonalData(
        @PathVariable id: Int,
        @RequestBody updatedPersonalData: PersonalDataEntity
    ): PersonalDataEntity {
        return personalDataService.update(id, updatedPersonalData) { existing, updated ->
            existing.copy(
                name = updated.name,
                lastname = updated.lastname,
                patronymic = updated.patronymic,
                phoneNumber = updated.phoneNumber
            )
        }
    }

    // Создание новых персональных данных
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPersonalData(@RequestBody personalData: PersonalDataEntity): PersonalDataEntity {
        return personalDataService.save(personalData)
    }

    // Удаление персональных данных
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePersonalData(@PathVariable id: Int) {
        personalDataService.deleteById(id)
    }
}
