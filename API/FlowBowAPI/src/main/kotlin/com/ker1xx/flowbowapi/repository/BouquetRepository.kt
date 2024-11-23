package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.BouquetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BouquetRepository : JpaRepository<BouquetEntity, Int> {
    fun findByName(name: String): BouquetEntity?
}
