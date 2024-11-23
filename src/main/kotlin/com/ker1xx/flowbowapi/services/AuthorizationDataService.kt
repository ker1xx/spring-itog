package com.ker1xx.flowbowapi.services

import com.ker1xx.flowbowapi.entity.AuthorizationDataEntity
import com.ker1xx.flowbowapi.entity.ClientEntity
import com.ker1xx.flowbowapi.repository.AdministratorRepository
import com.ker1xx.flowbowapi.repository.AuthorizationDataRepository
import com.ker1xx.flowbowapi.repository.ClientRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

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

    fun verifyPassword(user: AuthorizationDataEntity?, password: String, salt: String?): Boolean {
        val hashedPassword = hashPassword(password, salt)
        return hashedPassword == user?.password
    }

    private fun hashPassword(password: String?, salt: String?): String {
        val saltedString = password + salt

        // Получить экземпляр MessageDigest для алгоритма SHA-256
        val digest = MessageDigest.getInstance("SHA-256")

        // Хешировать строку
        val hashBytes = digest.digest(saltedString.toByteArray())

        // Закодировать хеш в Base64 для удобства хранения
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}
