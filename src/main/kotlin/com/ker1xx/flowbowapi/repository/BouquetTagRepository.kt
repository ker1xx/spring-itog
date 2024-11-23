package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BouquetTagRepository : JpaRepository<BouquetTagEntity, BouquetTagKey> {

    // Найти по составному ключу
    fun findByBouquetIdAndTagId(bouquetId: Int, tagId: Int): BouquetTagEntity?

    // Найти все по bouquetId
    fun findByBouquetId(bouquetId: Int): List<BouquetTagEntity> ?

    // Найти все по tagId
    fun findByTagId(tagId: Int): List<BouquetTagEntity> ?

}

