package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.services.AuthorizationDataService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authorization-data")
class AuthorizationDataController(private val authorizationDataService: AuthorizationDataService) {

    // Получение всех данных авторизации
    @GetMapping
    fun getAllAuthorizationData(): List<AuthorizationDataEntity> {
        return authorizationDataService.getAll()
    }

    // Получение данных авторизации по ID
    @GetMapping("/{id}")
    fun getAuthorizationDataById(@PathVariable id: Int): AuthorizationDataEntity {
        return authorizationDataService.getById(id)
    }

    // Обновление данных авторизации по ID
    @PutMapping("/{id}")
    fun updateAuthorizationData(
        @PathVariable id: Int,
        @RequestBody updatedAuthorizationData: AuthorizationDataEntity
    ): AuthorizationDataEntity {
        return authorizationDataService.update(id, updatedAuthorizationData)
    }

    // Создание новых данных авторизации
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthorizationData(@RequestBody authorizationData: AuthorizationDataEntity): AuthorizationDataEntity {
        return authorizationDataService.save(authorizationData)
    }

    // Удаление данных авторизации по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthorizationData(@PathVariable id: Int) {
        authorizationDataService.deleteById(id)
    }
}
