package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FlowerTagRepository : JpaRepository<FlowerTagEntity, FlowerTagKey>{

    fun findByFlowerIdAndTagId(flowerId: Int, tagId: Int): FlowerTagEntity?

    fun findByFlowerId(flowerId: Int): List<FlowerTagEntity> ?

    fun findByTagId(tagId: Int): List<FlowerTagEntity>?
}
