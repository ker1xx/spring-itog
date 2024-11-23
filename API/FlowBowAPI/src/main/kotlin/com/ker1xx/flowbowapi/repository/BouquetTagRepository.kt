package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BouquetTagRepository : JpaRepository<BouquetTagEntity, BouquetTagKey> {

    fun findByBouquetIdAndTagId(bouquetId: Int, tagId: Int): BouquetTagEntity?

    fun findByBouquetId(bouquetId: Int): List<TagEntity>

    fun findByTagId(tagId: Int): List<BouquetEntity>

}
