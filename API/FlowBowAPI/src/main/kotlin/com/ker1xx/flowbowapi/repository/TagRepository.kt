package com.ker1xx.flowbowapi.repository

import com.ker1xx.flowbowapi.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<TagEntity, Int>{
    fun findByName(name: String): TagEntity?

}
