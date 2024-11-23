package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.FlowerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FlowerRepository : JpaRepository<FlowerEntity, Int> {

    fun findByName(name: String): FlowerEntity
}
