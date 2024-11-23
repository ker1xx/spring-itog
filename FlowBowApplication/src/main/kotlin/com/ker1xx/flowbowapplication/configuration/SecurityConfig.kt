package com.ker1xx.flowbowapplication.configuration

import com.ker1xx.flowbowapplication.configuration.jwt.JwtAuthenticationFilter
import com.ker1xx.flowbowapplication.configuration.jwt.JwtCookieFilter
import com.ker1xx.flowbowapplication.configuration.jwt.JwtService
import com.ker1xx.flowbowapplication.configuration.jwt.JwtTokenAddFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Suppress("DEPRECATION")
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtTokenAddFilter: JwtTokenAddFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeHttpRequests { auth ->
                auth
                    // Разрешаем доступ к Swagger
                    .requestMatchers(
                        "/",
                        "/registration/**",
                        "/login/**",
                        "/error/**",
                        "/static/**",
                        "/auth/**"
                    ).permitAll()
                    // Доступ к публичным эндпоинтам
                    .requestMatchers(
                        HttpMethod.GET,
                        "/tags/**",
                        "/flowers/**",
                        "/bouquets/**",
                        "/bouquets/**",
                        "/bouquets-flower/**",
                        "/bouquets-tags/**",
                        "/flowers-tags/**",
                        "/secretpage/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.POST, "/authorization-data/registration").permitAll()
                    // Все остальные запросы требуют авторизации
                    .anyRequest().permitAll()
            }

        return http.build()
    }
}