package com.ker1xx.flowbowapplication.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminController {
    @GetMapping("/admin")
    fun showAdminDashboard(): String {
        return "admin/admin"
    }

    @GetMapping("/admin/manage-users")
    fun manageUsers(): String {
        // Логика для управления пользователями
        return "admin_manage_users"
    }

    @GetMapping("/admin/manage-orders")
    fun manageOrders(): String {
        // Логика для управления заказами
        return "admin_manage_orders"
    }
}