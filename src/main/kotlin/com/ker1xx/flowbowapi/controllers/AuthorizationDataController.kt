package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.dto.CheckForDuplicateDTO
import com.ker1xx.flowbowapi.dto.LoginResponseDTO
import com.ker1xx.flowbowapi.dto.RegistrationResponseDTO
import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.repository.AdministratorRepository
import com.ker1xx.flowbowapi.repository.ClientRepository
import com.ker1xx.flowbowapi.services.AuthorizationDataService
import com.ker1xx.flowbowapi.services.helpers.RoleService
import com.ker1xx.flowbowapi.utility.jwt.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authorization-data")
class AuthorizationDataController(
    private val authorizationDataService: AuthorizationDataService,
    private val roleService: RoleService
) {

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

    @Suppress("UNREACHABLE_CODE", "ALWAYS_NULL")
    @PostMapping("/authorization")
    @ResponseStatus(HttpStatus.OK)
    fun authorization(@RequestBody request: AuthorizationDataEntity): ResponseEntity<Any> {
        val user = authorizationDataService.findDuplicateEntity(request)
        val passcheck = authorizationDataService.verifyPassword(user, request.password, user?.salt)
        if (user == null || !passcheck) {
            return ResponseEntity.status(401).body("Invalid login or password")
        }

        var role = roleService.getRole(
            authorizationData = user!!
        )

        if (user.login == "user")
            role = "bonus"

        val token = JwtTokenProvider.createJwtToken(user.login, role!!)

        val response = LoginResponseDTO(
            token = token,
            role = role,
            id = user.id,
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registration(@RequestBody request: AuthorizationDataEntity): ResponseEntity<Any> {
        val user = authorizationDataService.findDuplicateEntity(request)

        // Проверка на уникальность
        if (user != null) {
            return ResponseEntity.status(401).body("This data is already used")
        }

        // Сохраняем нового пользователя
        val savedUser = authorizationDataService.save(request)

        val role = "client"

        // Генерируем JWT токен
        val token = JwtTokenProvider.createJwtToken(savedUser.login, "client")

        // Формируем и возвращаем ответ с ID, объектом и токеном
        val response = RegistrationResponseDTO(
            id = savedUser.id,
            token = token
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/check-for-duplicates")
    @ResponseStatus(HttpStatus.OK)
    suspend fun checkForDuplicates(@RequestBody request: AuthorizationDataEntity): ResponseEntity<CheckForDuplicateDTO> {
        val user = authorizationDataService.findDuplicateEntity(request)
        val responseMessage = if (user != null) {
            CheckForDuplicateDTO("Found", true)
        } else {
            CheckForDuplicateDTO("Not found", false)
        }
        return ResponseEntity.ok(responseMessage)
    }
}
