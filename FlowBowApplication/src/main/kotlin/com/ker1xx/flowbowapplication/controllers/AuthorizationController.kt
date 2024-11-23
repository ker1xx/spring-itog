package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.dto.AuthorizationDataDTO
import com.ker1xx.flowbowapplication.models.dto.LoginResponseDTO
import com.ker1xx.flowbowapplication.utility.Constants
import com.ker1xx.flowbowapplication.utility.Resource
import com.ker1xx.flowbowapplication.web.ApiService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AuthorizationController(
    private val apiService: ApiService
) {

    // Страница для ввода логина и пароля
    @GetMapping("/auth")
    fun showLoginForm(model: Model): String {
        return "authorization"  // HTML страница с формой для авторизации
    }

    // POST запрос для обработки авторизации
    @PostMapping("/auth")
    fun handleLogin(
        @RequestParam login: String,
        @RequestParam password: String,
        model: Model,
        session: HttpSession,
        response: HttpServletResponse
    ): String = runBlocking {
        val authorizationData = AuthorizationDataDTO(
            id = null,
            login = login,
            password = password, // Пароль будет обработан на сервере
            salt = ""            // Не требуется на этом этапе
        )

        val loginResponse = authenticateUser(authorizationData)
        if (loginResponse != null) {
            session.setAttribute("jwtToken", loginResponse.token)
            session.setAttribute("userRole", loginResponse.role)
            session.setAttribute("userId", loginResponse.id)

            val authentication = SecurityContextHolder.getContext().authentication
            println("Current user: ${authentication?.name}, Roles: ${authentication?.authorities}")

            val cookie = Cookie("jwtToken", loginResponse.token)
            cookie.isHttpOnly = true       // Защищает куки от доступа через JavaScript
            cookie.maxAge = 60 * 60        // Срок действия 24 часа
            cookie.path = "/"              // Доступно для всех эндпоинтов
            response.addCookie(cookie)
            if (loginResponse.role == "administrator") {
                "redirect:/admin"  // Перенаправление на главную страницу администратора после успешного входа
            }
            else{
                "redirect:/"
            }
        } else {
            model.addAttribute("error", "Invalid login or password")
            "auth"
        }
    }

    private suspend fun authenticateUser(dto: AuthorizationDataDTO): LoginResponseDTO? {
        val url = "${Constants.BASE_URL}authorization-data/authorization"
        return when (val response = apiService.sendPostRequestToApi(url, dto, null)) {
            is Resource.Success -> response.data?.let { data ->
                try {
                    Gson().fromJson(data, LoginResponseDTO::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            else -> null
        }
    }
}