package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.BouquetFlowerEntity
import com.ker1xx.flowbowapi.entity.BouquetFlowerKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BouquetFlowerRepository : JpaRepository<BouquetFlowerEntity, BouquetFlowerKey>{

    fun findByBouquetIdAndFlowerId(bouquetId: Int, flowerId: Int): BouquetFlowerEntity?

    fun findByBouquetId(bouquetId: Int): List<BouquetFlowerEntity> ?

    fun findByFlowerId(flowerId: Int): List<BouquetFlowerEntity> ?
}
