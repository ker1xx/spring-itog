package com.ker1xx.flowbowapi.controllers

import com.ker1xx.flowbowapi.services.AdministratorService
import com.ker1xx.flowbowapi.entity.AdministratorEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/administrators")
class AdministratorController(private val administratorService: AdministratorService) {

    @GetMapping("/{id}")
    fun getAdministratorById(@PathVariable id: Int): AdministratorEntity {
        return administratorService.getById(id)
    }

    @GetMapping
   suspend fun getAllAdministrators(): List<AdministratorEntity> {
        return administratorService.getAll()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAdministrator(@RequestBody administrator: AdministratorEntity): AdministratorEntity {
        return administratorService.save(administrator)
    }

    @PutMapping("/{id}")
    fun updateAdministrator(
        @PathVariable id: Int,
        @RequestBody updatedAdministrator: AdministratorEntity
    ): AdministratorEntity {
        return administratorService.update(id, updatedAdministrator)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAdministrator(@PathVariable id: Int) {
        administratorService.deleteById(id)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAdministrator(@RequestBody administrator: AdministratorEntity) {
        administratorService.delete(administrator)
    }
}
