package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.entity.ClientEntity
import com.ker1xx.flowbowapi.services.ClientService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/clients")
class ClientController(private val clientService: ClientService) {

    // Получение всех клиентов
    @GetMapping
    fun getAllClients(): List<ClientEntity> {
        return clientService.getAll()
    }

    // Получение клиента по ID
    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Int): ClientEntity {
        return clientService.getById(id)
    }

    // Обновление информации о клиенте
    @PutMapping("/{id}")
    fun updateClient(
        @PathVariable id: Int,
        @RequestBody updatedClient: ClientEntity
    ): ClientEntity {
        return clientService.update(id, updatedClient)
    }

    // Создание нового клиента
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createClient(@RequestBody client: ClientEntity): ClientEntity {
        return clientService.save(client)
    }

    // Удаление клиента по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteClient(@PathVariable id: Int) {
        clientService.deleteById(id)
    }
}
