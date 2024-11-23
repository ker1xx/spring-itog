package com.ker1xx.flowbowapplication.configuration.jwt

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JwtTokenAddFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        // Приводим request и response к нужным типам
        val httpRequest = request as? HttpServletRequest
        val httpResponse = response as? HttpServletResponse

        if (httpRequest != null && httpResponse != null) {
            val token = getJwtFromSession(httpRequest)

            if (token != null) {
                httpResponse.setHeader("Authorization", "Bearer $token")
            }

            chain?.doFilter(request, response) // Продолжаем цепочку фильтров
        }
    }

    private fun getJwtFromSession(request: HttpServletRequest): String? {
        return request.session.getAttribute("jwtToken") as? String
    }
}
