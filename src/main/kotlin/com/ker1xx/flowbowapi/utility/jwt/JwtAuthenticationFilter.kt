package com.ker1xx.flowbowapi.utility.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestUri = request.requestURI
        println("Request URI: $requestUri") // Лог запросов
        val requestMethod = request.method

        // Пропустить публичные эндпоинты
        if ((requestUri.startsWith("/swagger-ui")
            || requestUri.startsWith("/v3/api-docs")
            || requestUri.startsWith("/swagger-resources")
            || requestUri.startsWith("/webjars")
            || requestUri.startsWith("/tags")
            || requestUri.startsWith("/flowers")
            || requestUri.startsWith("/bouquets"))
            && requestMethod == "GET"
            || requestUri.startsWith("/authorization-data")
        ) {
            println("Swagger request allowed: $requestUri")
            println("Response: $response")
            filterChain.doFilter(request, response)
            return
        }
        else{
            println("Swagger request doesn't allowed: $requestUri")
        }

        // Проверка наличия токена
        val token = request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (token == null || !JwtTokenProvider.validateJwtToken(token)) {
            println("Invalid or missing token for $requestUri")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token")
            return
        }

        // Установка пользователя в SecurityContext
        val username = JwtTokenProvider.getUsernameFromToken(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(username, null, null)

        filterChain.doFilter(request, response)
    }
}
