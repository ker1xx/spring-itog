package com.ker1xx.flowbowapplication.controllers

import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MainScreenController {
    // Главная страница
    @GetMapping("/")
    fun index(
        @RequestParam(required = false) error: String?, // Параметр ошибки для попапа
        model: Model,
        session: HttpSession
    ): String {
        // Добавление параметра для отображения ошибки, если он передан в URL
        if (error != null) {
            model.addAttribute("error", error)
        }
        val token = session.getAttribute("jwtToken")
        println("JWT Token in session after login: $token")
        return "index"  // Указывает на index.html, который рендерится через Thymeleaf
    }
}