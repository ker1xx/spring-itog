package com.ker1xx.flowbowapplication.configuration.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secretString: String
) {

    private fun createKeyFromString(secretString: String): SecretKey {
        val sha256 = MessageDigest.getInstance("SHA-256")
        val hash = sha256.digest(secretString.toByteArray(StandardCharsets.UTF_8))
        return Keys.hmacShaKeyFor(hash) // Преобразуем хэш в ключ
    }

    // Валидация токена
    fun isValidToken(token: String): Boolean {
        return try {
            val secretKey: SecretKey = createKeyFromString(secretString)
            val claims = getClaimsFromToken(token)
            val expiration = claims.expiration
            !expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    // Извлечение имени пользователя из токена
    fun extractUsername(token: String): String {
        return getClaimsFromToken(token).subject
    }

    // Извлечение даты истечения токена
    private fun getExpirationDateFromToken(token: String): Date {
        val claims = getClaimsFromToken(token)
        return claims.expiration
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaimsFromToken(token)
        println(claims)
        val authorities = claims["role"]?.toString()?.let { listOf(SimpleGrantedAuthority("ROLE_$it")) }
        return UsernamePasswordAuthenticationToken(claims.subject, null, authorities)
    }

    private fun getClaimsFromToken(token: String): Claims {
        // Достаём claims из токена
        val secretKey = createKeyFromString(secretString)
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    // Метод для получения Claims из токена, который хранится в сессии
    fun getClaimsFromSession(request: HttpServletRequest): Claims? {
        // Извлекаем токен из сессии
        val jwtToken = request.session.getAttribute("jwtToken") as String?

        // Проверяем, есть ли токен в сессии
        if (jwtToken != null) {
            // Создаем ключ из строки
            val secretKey: SecretKey = createKeyFromString(secretString)

            try {
                // Извлекаем данные (Claims) из токена
                return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .body
            } catch (e: Exception) {
                // Логируем или обрабатываем ошибку, если токен недействителен
                println("Error parsing JWT: ${e.message}")
            }
        } else {
            println("JWT token not found in session")
        }

        return null // Если токен не найден или произошла ошибка
    }
}