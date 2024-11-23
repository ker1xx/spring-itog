package com.ker1xx.flowbowapi.utility.jwt
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.Date
import io.jsonwebtoken.Jwts
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.SecretKey

object JwtTokenProvider {

    private val secretString = "secretkey1234567891011121314151617181920"

    private fun createKeyFromString(secretString: String): SecretKey {
        val sha256 = MessageDigest.getInstance("SHA-256")
        val hash = sha256.digest(secretString.toByteArray(StandardCharsets.UTF_8))
        return Keys.hmacShaKeyFor(hash) // Преобразуем хэш в ключ
    }

    fun createJwtToken(username: String, role: String): String {
        val secretKey: SecretKey = createKeyFromString(secretString)
        val expirationTime = 1000 * 60 * 60 * 24 // 12 часов в миллисекундах
        return Jwts.builder()
            .setSubject(username) // Основной субъект токена
            .setIssuer("FlowBow") // Укажите вашего провайдера
            .claim("role", role)
            .setIssuedAt(Date()) // Время создания
            .setExpiration(Date(System.currentTimeMillis() + expirationTime)) // Время истечения
            .signWith(secretKey) // Подпись с секретным ключом
            .compact()
    }


    fun validateJwtToken(token: String): Boolean {
        return try {
            val secretKey: SecretKey = createKeyFromString(secretString)
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true // Токен валиден
        } catch (e: Exception) {
            false // Токен недействителен
        }
    }

    fun getUsernameFromToken(token: String): String? {
        val secretKey: SecretKey = createKeyFromString(secretString)
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
        return claims.subject // Возвращает имя пользователя
    }
}