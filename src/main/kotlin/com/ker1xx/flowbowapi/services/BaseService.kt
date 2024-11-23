package com.ker1xx.flowbowapi.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository

abstract class BaseService<T : Any, ID : Any>(
    private val repository: JpaRepository<T, ID>
) {

    // Метод для получения сущности по ID
    fun getById(id: ID): T {
        val entity = repository.findById(id)
        if (entity.isPresent) {
            return entity.get()
        } else {
            throw EntityNotFoundException("Entity not found with id: $id")
        }
    }

    // Метод для получения всех сущностей
    fun getAll(): List<T> {
        return repository.findAll()
    }

    // Метод для сохранения с проверкой на дублирование по всем полям, кроме ID
    fun save(entity: T): T {
        // Получаем все сущности с такими же значениями полей, кроме ID
        val existingEntity = findDuplicateEntity(entity)

        if (existingEntity != null) {
            throw IllegalArgumentException("Entity with the same attributes already exists")
        }

        return repository.save(entity)
    }

    fun update(id: ID, updatedEntity: T, updateFunction: (T, T) -> T): T {
        val existingEntity = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Entity with ID $id not found") }

        val newEntity = updateFunction(existingEntity, updatedEntity)
        return repository.save(newEntity)
    }

    // Метод для удаления сущности
    fun deleteById(id: ID) {
        if (repository.existsById(id)) {
            repository.deleteById(id)
        } else {
            throw EntityNotFoundException("Entity not found with id: $id")
        }
    }

    // Базовый метод для удаления сущности по объекту
    fun delete(entity: T) {
        if (repository.existsById(getEntityId(entity))) {
            repository.delete(entity)
        } else {
            throw EntityNotFoundException("Entity not found")
        }
    }

    // Метод для получения ID сущности (можно переопределить, если ID не в стандартном поле)
    open fun getEntityId(entity: T): ID {
        throw NotImplementedError("Please implement getEntityId() method in the service")
    }

    // Абстрактный метод для поиска дубликатов на основе логики для конкретной сущности
    abstract fun findDuplicateEntity(entity: T): T?




}