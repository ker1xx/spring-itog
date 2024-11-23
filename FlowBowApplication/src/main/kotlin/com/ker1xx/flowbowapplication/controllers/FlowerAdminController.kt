package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.customEntities.FlowerWithTagDisplayEntity
import com.ker1xx.flowbowapplication.models.dto.*
import com.ker1xx.flowbowapplication.utility.Constants
import com.ker1xx.flowbowapplication.web.ApiService
import com.ker1xx.flowbowapplication.utility.Resource
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.WebDataBinder
import java.beans.PropertyEditorSupport

@Controller
@RequestMapping("/admin/flowers")
class FlowerAdminController(private val apiService: ApiService, private val gson: Gson) {

    @GetMapping
    fun showFlowers(model: Model): String = runBlocking {
        val flowers = getFlowers()

        val flowersWithTags = flowers.map { flower ->

            val flowerTags = findTagsByFlowerId(flower.id!!)

            FlowerWithTagDisplayEntity(
                id = flower.id,
                name = flower.name,
                price = flower.price,
                amount = flower.amount,
                isAvailable = flower.isAvailable,
                tags = flowerTags.map { it.tag }
            )
        }
        model.addAttribute("flowers", flowersWithTags)
        return@runBlocking "admin/flowers"
    }

    @GetMapping("/new")
    suspend fun showCreateForm(model: Model): String {
        val flower = FlowerWithTagDisplayEntity(id = 0, name = "", price = 0f, amount = 0, isAvailable = true)
        val tags = getTags()
        flower.tags = tags
        model.addAttribute("flower", flower)
        model.addAttribute("tags", tags)
        return "admin/flower_create"
    }


    @PostMapping("/new")
    fun createFlowerWithTags(
        @ModelAttribute flower: FlowerDTO,
        @RequestParam(required = false) tagIds: List<Int>?,
        session: HttpSession
    ): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}flowers"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendPostRequestToApi(url, flower, headers)

        if (response is Resource.Success) {
            val createdFlower = gson.fromJson(response.data, FlowerDTO::class.java)
            tagIds?.forEach { tagIds ->
                addTagToFlower(createdFlower, TagDTO(1, "hi"), session)
            }
            return@runBlocking "redirect:/admin/flowers"
        } else {
            return@runBlocking "admin/flower_create"
        }
    }

    @GetMapping("/{id}/edit")
    fun showEditForm(
        @PathVariable id: Int,
        model: Model
    ): String = runBlocking {
        val flower = getFlowerById(id)
        val flowerToDisplay = FlowerWithTagDisplayEntity(
            id = flower!!.id,
            name = flower.name,
            price = flower.price,
            amount = flower.amount,
            isAvailable = flower.isAvailable,
            tags = findTagsByFlowerId(id).map { it.tag }
        )
        val tags = getTags()
        model.addAttribute("flower", flowerToDisplay)
        model.addAttribute("tags", tags)
        model.addAttribute("selectedTags", flowerToDisplay.tags.map { it.id })
        return@runBlocking "admin/flower_edit"
    }

    @PostMapping("/{id}/edit")
    fun editFlower(
        @PathVariable id: Int,
        @ModelAttribute flowerToDisplay: FlowerWithTagDisplayEntity,
        @RequestParam(required = false) tagIds: List<TagDTO>?,
        session: HttpSession
    ): String =
        runBlocking {
            val jwtToken = session.getAttribute("jwtToken") as String?
            val flower = FlowerDTO(
                id = flowerToDisplay.id,
                name = flowerToDisplay.name,
                price = flowerToDisplay.price,
                amount = flowerToDisplay.amount,
                isAvailable = flowerToDisplay.isAvailable
            )
            if (jwtToken.isNullOrEmpty()) {
                return@runBlocking "redirect:/login"
            }

            val url = "${Constants.BASE_URL}flowers/$id"
            val headers = mapOf("Authorization" to "Bearer $jwtToken")
            val response = apiService.sendPutRequestToApi(url, flower, headers)

            if (response is Resource.Success) {
                val existingTags = getFlowerTags(id).mapNotNull { it.tag }
                val tagsToAdd = flowerToDisplay.tags
                val tagsToRemove = existingTags.filterNot { existingTag -> tagIds?.contains(existingTag) == true }

                tagsToAdd.forEach { addTagToFLower(flower, it, session) }
                tagsToRemove.forEach { removeTagFromFlower(id, it.id!!, session) }

                return@runBlocking "redirect:/admin/flowers"
            } else {
                return@runBlocking "admin/flower_edit"
            }
        }

    private suspend fun removeTagFromFlower(flowerId: Int, tagId: Int, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendDeleteRequestToApi("${Constants.BASE_URL}bouquets-tags/${flowerId}/${tagId}", headers)
    }

    private suspend fun addTagToFLower(flower: FlowerDTO, tag: TagDTO, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        if (jwtToken.isNullOrEmpty()) return

        val flowerTagDTO = FlowerTagDTO(
            flower = flower,
            tag = tag
        )

        val url = "${Constants.BASE_URL}flowers-tags"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendPostRequestToApi(url, flowerTagDTO, headers)
    }

    private suspend fun getFlowerTags(id: Int): List<BouquetTagDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}flowers-tags/flower/${id}", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<BouquetTagDTO>::class.java).toList()
        } else emptyList()
    }

    @GetMapping("/{id}/delete")
    fun deleteFlower(@PathVariable id: Int, session: HttpSession): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}flowers/$id"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendDeleteRequestToApi(url, headers)

        if (response is Resource.Success) {
            return@runBlocking "redirect:/admin/flowers"
        } else {
            return@runBlocking "admin/flowers"
        }
    }

    private suspend fun getFlowers(): List<FlowerDTO> {
        val url = "${Constants.BASE_URL}flowers"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                val flowerArray = gson.fromJson(response.data, Array<FlowerDTO>::class.java)
                flowerArray.toList()
            }

            else -> emptyList()
        }
    }

    private suspend fun getFlowerById(id: Int): FlowerDTO? {
        val url = "${Constants.BASE_URL}flowers/$id"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                gson.fromJson(response.data, FlowerDTO::class.java)
            }

            else -> null
        }
    }

    private suspend fun findTagsByFlowerId(flowerId: Int): List<FlowerTagDTO> {
        val url = "${Constants.BASE_URL}flowers-tags/flower/${flowerId}"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в список объектов TagDTO
                gson.fromJson(response.data, Array<FlowerTagDTO>::class.java).toList()
            }

            else -> emptyList()
        }

    }

    private suspend fun getTags(): List<TagDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}tags", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<TagDTO>::class.java).toList()
        } else emptyList()
    }

    private suspend fun addTagToFlower(flower: FlowerDTO?, tag: TagDTO, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        if (jwtToken.isNullOrEmpty()) return

        val flowerTagDTO = FlowerTagDTO(
            flower = flower!!,
            tag = tag
        )

        val url = "${Constants.BASE_URL}flower-tags"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendPostRequestToApi(url, flowerTagDTO, headers)
    }

    suspend fun getTagsByIds(tagIds: List<Int>): List<TagDTO> {
        return getTags().filter { tag -> tagIds.contains(tag.id) }
    }

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
            List::class.java,
            "tags",
            object : PropertyEditorSupport() {
                override fun setAsText(text: String) {
                    runBlocking {
                        val tagIds = text.split(",").mapNotNull { it.toIntOrNull() }
                        val tags = getTagsByIds(tagIds)
                        value = tags
                    }
                }
            }
        )
    }
}
