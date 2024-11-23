package com.ker1xx.flowbowapplication.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SecretpageController {
    @GetMapping("/secretpage")
    fun showsecretpage(): String {
        return "secretpage"
    }
}