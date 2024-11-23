package com.ker1xx.flowbowapplication.models.helpers

import java.security.MessageDigest
import java.util.Base64
import java.security.SecureRandom

class Hashing {
    fun hashWithSalt(stringToHash: String, salt : String): String {
        // Сконкатенировать логин и соль
        val saltedString = stringToHash + salt

        // Получить экземпляр MessageDigest для алгоритма SHA-256
        val digest = MessageDigest.getInstance("SHA-256")

        // Хешировать строку
        val hashBytes = digest.digest(saltedString.toByteArray())

        // Закодировать хеш в Base64 для удобства хранения
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    fun generateRandomSalt() : String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?/"
        val random = SecureRandom()
        val salt = StringBuilder(4)

        for (i in 0 until 4) {
            val randomIndex = random.nextInt(chars.length)
            salt.append(chars[randomIndex])
        }

        // Возвращаем соль как строку, закодированную в Base64
        return salt.toString()
    }
}