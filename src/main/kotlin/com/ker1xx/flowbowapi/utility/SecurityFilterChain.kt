@file:Suppress("DEPRECATION")

package com.ker1xx.flowbowapi.utility

import com.ker1xx.flowbowapi.utility.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebSecurity
@Configuration
@EnableWebMvc
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.POST, "/authorization-data/**").permitAll()
                    // Разрешаем доступ к Swagger
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll()
                    // Доступ к публичным эндпоинтам
                    .requestMatchers(
                        HttpMethod.GET,
                        "/tags/**",
                        "/flowers/**",
                        "/bouquets/**",
                        "/bouquets/**",
                        "/bouquets-flowers/**",
                        "/bouquets-tags/**",
                        "/flowers-tags/**",
                    ).permitAll()
                    // Все остальные запросы требуют авторизации
                    .anyRequest().authenticated()
            }
            // Отключаем стандартную форму входа и Basic Auth
            .formLogin().disable()
            .httpBasic().disable()
            // Добавляем ваш JWT фильтр
            .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}