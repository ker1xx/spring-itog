package com.ker1xx.flowbowapplication.configuration.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtCookieFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cookies = request.cookies?.associateBy { it.name }
        val jwtToken = cookies?.get("jwtToken")?.value

        if (jwtToken != null) {
            try {
                if (jwtService.isValidToken(jwtToken)) {
                    val authentication = jwtService.getAuthentication(jwtToken)
                    SecurityContextHolder.getContext().authentication = authentication
                    println("Authentication set: ${authentication.name}, Roles: ${authentication.authorities}")
                } else {
                    println("Invalid JWT Token")
                }
            } catch (ex: Exception) {
                println("Error processing JWT: ${ex.message}")
            }
        } else {
            println("JWT Token not found in cookies")
        }

        filterChain.doFilter(request, response)
    }
}