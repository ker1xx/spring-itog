package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.customEntities.BouquetWithTagDisplayEntity
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
@RequestMapping("/admin/bouquets")
class BouquetAdminController(private val apiService: ApiService, private val gson: Gson) {

    @GetMapping
    fun showBouquets(model: Model): String = runBlocking {
        val bouquets = getBouquets()

        // Создаем новый список для объектов BouquetWithTagDisplayEntity
        val bouquetsWithTags = bouquets.map { bouquet ->
            // Получаем теги для букета с использованием репозитория
            val bouquetTags = findTagsByBouquetId(bouquet.id!!)
            val flowersInBouquet = findFlowersByBouquetId(bouquet.id)
            // Создаем объект BouquetWithTagDisplayEntity для каждого букета
            BouquetWithTagDisplayEntity(
                id = bouquet.id,                            // Присваиваем ID букета
                name = bouquet.name,                        // Присваиваем название букета
                price = bouquet.price,                      // Присваиваем цену
                amount = bouquet.amount,                    // Присваиваем количество
                isAvailable = bouquet.isAvailable,          // Присваиваем доступность
                tags = bouquetTags.mapNotNull { it.tag },   // Присваиваем список тегов
                flowers = flowersInBouquet.map { it.flower }
            )
        }

        model.addAttribute("bouquets", bouquetsWithTags)
        return@runBlocking "admin/bouquets"
    }

    @GetMapping("/new")
    suspend fun showCreateForm(model: Model): String {
        val bouquet = BouquetWithTagDisplayEntity(id = 0, name = "", price = 0f, amount = 0, isAvailable = true)
        val tags = getTags() // Получаем список всех доступных тегов
        val flowers = getFlowers()
        bouquet.tags = tags
        bouquet.flowers = flowers
        model.addAttribute("bouquet", bouquet)
        model.addAttribute("tags", tags)
        model.addAttribute("flowers", flowers)
        return "admin/bouquet_create"
    }

    @PostMapping("/new")
    fun createBouquetWithTags(
        @ModelAttribute bouquet: BouquetDTO,
        @RequestParam(required = false) tagIds: List<Int>?,
        @RequestParam(required = false) flowerIds: List<Int>?,
        session: HttpSession
    ): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) return@runBlocking "redirect:/login"

        val bouquetResponse = apiService.sendPostRequestToApi(
            "${Constants.BASE_URL}bouquets", bouquet, mapOf("Authorization" to "Bearer $jwtToken")
        )

        if (bouquetResponse is Resource.Success) {
            val createdBouquet = gson.fromJson(bouquetResponse.data, BouquetDTO::class.java)
            tagIds?.forEach { tagId ->

                getTagById(tagId)?.let {
                    addTagToBouquet(
                        createdBouquet,
                        it,
                        session
                    )
                } // Добавляем теги к созданному букету
            }
            flowerIds?.forEach { flowerId ->
                getFlowerById(flowerId)?.let {
                    addFlowerToBouquet(
                        createdBouquet,
                        it,
                        session
                    )
                } // Добавляем теги к созданному букету
            }
            return@runBlocking "redirect:/admin/bouquets"
        } else {
            return@runBlocking "admin/bouquet_create"
        }
    }

    private suspend fun getFlowerById(flowerId: Int): FlowerDTO? {
        val url = "${Constants.BASE_URL}flowers/$flowerId"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в объект BouquetDTO
                gson.fromJson(response.data, FlowerDTO::class.java)
            }

            else -> null
        }
    }

    private suspend fun getTagById(tagId: Int): TagDTO? {
        val url = "${Constants.BASE_URL}tags/$tagId"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в объект BouquetDTO
                gson.fromJson(response.data, TagDTO::class.java)
            }

            else -> null
        }
    }

    @GetMapping("/{id}/edit")
    fun showEditForm(@PathVariable id: Int, model: Model): String = runBlocking {
        val bouquet = getBouquetById(id)
        val bouquetToDisplay = BouquetWithTagDisplayEntity(
            id = bouquet!!.id,
            name = bouquet.name,
            price = bouquet.price,
            amount = bouquet.amount,
            isAvailable = bouquet.isAvailable,
            tags = findTagsByBouquetId(id).mapNotNull { it.tag }, // Получаем теги для текущего букета
            flowers = findFlowersByBouquetId(id).map { it.flower }
        )
        val tags = getTags()
        val flowers = getFlowers()
        model.addAttribute("bouquet", bouquetToDisplay)
        model.addAttribute("tags", tags)
        model.addAttribute("selectedTags", bouquetToDisplay.tags.map { it.id })
        model.addAttribute("flowers", flowers)
        model.addAttribute("selectedFlowers", bouquetToDisplay.flowers.map { it.id })
        return@runBlocking "admin/bouquet_edit"
    }

    @PostMapping("/{id}/edit")
    fun editBouquetWithTags(
        @PathVariable id: Int,
        @ModelAttribute bouquetToDisplay: BouquetWithTagDisplayEntity,
        @RequestParam(required = false) tagIds: List<TagDTO>?,
        @RequestParam(required = false) flowerIds: List<FlowerDTO>?,
        session: HttpSession
    ): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?
        val bouquet = BouquetDTO(
            id = bouquetToDisplay.id,
            name = bouquetToDisplay.name,
            price = bouquetToDisplay.price,
            amount = bouquetToDisplay.amount,
            isAvailable = bouquetToDisplay.isAvailable
        )
        if (jwtToken.isNullOrEmpty()) return@runBlocking "redirect:/login"

        val response = apiService.sendPutRequestToApi(
            "${Constants.BASE_URL}bouquets/$id", bouquet, mapOf("Authorization" to "Bearer $jwtToken")
        )

        if (response is Resource.Success) {
            val existingTags = getBouquetTags(id).mapNotNull { it.tag }
            val tagsToAdd = bouquetToDisplay.tags
            val tagsToRemove = existingTags.filterNot { existingTag -> tagIds?.contains(existingTag) == true }

            tagsToAdd.forEach { addTagToBouquet(bouquet, it, session) }
            tagsToRemove.forEach { removeTagFromBouquet(id, it.id!!, session) }

            val existingFlowers = getBouquetFlowers(id).map { it.flower }
            val flowersToAdd = bouquetToDisplay.flowers
            val flowersToRemove =
                existingFlowers.filterNot { existingFlower -> flowerIds?.contains(existingFlower) == true }

            flowersToAdd.forEach { addFlowerToBouquet(bouquet, it, session) }
            flowersToRemove.forEach { removeFlowerFromBouquet(id, it.id!!, session) }

            return@runBlocking "redirect:/admin/bouquets"
        } else {
            return@runBlocking "admin/bouquet_edit"
        }
    }

    @GetMapping("/{id}/delete")
    fun deleteBouquet(@PathVariable id: Int, session: HttpSession): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}bouquets/$id"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendDeleteRequestToApi(url, headers)

        if (response is Resource.Success) {
            return@runBlocking "redirect:/admin/bouquets"
        } else {
            return@runBlocking "admin/bouquets"
        }
    }

    private suspend fun getBouquets(): List<BouquetDTO> {
        val url = "${Constants.BASE_URL}bouquets"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в список объектов BouquetDTO
                val bouquetArray = gson.fromJson(response.data, Array<BouquetDTO>::class.java)
                bouquetArray.toList()
            }

            else -> {
                emptyList<BouquetDTO>()
            }
        }
    }

    private suspend fun getBouquetById(id: Int): BouquetDTO? {
        val url = "${Constants.BASE_URL}bouquets/$id"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в объект BouquetDTO
                gson.fromJson(response.data, BouquetDTO::class.java)
            }

            else -> null
        }
    }

    // Методы для работы с тегами
    private suspend fun getTags(): List<TagDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}tags", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<TagDTO>::class.java).toList()
        } else emptyList()
    }

    // Методы для работы с цветами
    private suspend fun getFlowers(): List<FlowerDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}flowers", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<FlowerDTO>::class.java).toList()
        } else emptyList()
    }

    private suspend fun getBouquetTags(bouquetId: Int): List<BouquetTagDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}bouquets-tags/bouquet/${bouquetId}", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<BouquetTagDTO>::class.java).toList()
        } else emptyList()
    }

    private suspend fun getBouquetFlowers(bouquetId: Int): List<BouquetFlowerDTO> {
        val response =
            apiService.sendGetRequestToApi("${Constants.BASE_URL}bouquets-flowers/bouquet/${bouquetId}", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<BouquetFlowerDTO>::class.java).toList()
        } else emptyList()
    }


    private suspend fun removeTagFromBouquet(bouquetId: Int, tagId: Int, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendDeleteRequestToApi("${Constants.BASE_URL}bouquets-tags/${bouquetId}/${tagId}", headers)
    }

    private suspend fun removeFlowerFromBouquet(bouquetId: Int, flowerId: Int, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendDeleteRequestToApi("${Constants.BASE_URL}bouquets-flowers/${bouquetId}/${flowerId}", headers)
    }


    private suspend fun addTagToBouquet(bouquet: BouquetDTO, tag: TagDTO, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        if (jwtToken.isNullOrEmpty()) return

        val bouquetTagDTO = BouquetTagDTO(
            bouquet = bouquet,
            tag = tag
        )

        val url = "${Constants.BASE_URL}bouquets-tags"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendPostRequestToApi(url, bouquetTagDTO, headers)
    }

    private suspend fun addFlowerToBouquet(bouquet: BouquetDTO, flower: FlowerDTO, session: HttpSession) {
        val jwtToken = session.getAttribute("jwtToken") as String?
        if (jwtToken.isNullOrEmpty()) return

        val bouquetFlowerDTO = BouquetFlowerDTO(
            bouquet = bouquet,
            flower = flower
        )

        val url = "${Constants.BASE_URL}bouquets-flowers"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        apiService.sendPostRequestToApi(url, bouquetFlowerDTO, headers)
    }

    suspend fun findTagsByBouquetId(bouquetId: Int): List<BouquetTagDTO> {
        // Отправляем запрос к API для получения тегов по ID букета
        val url = "${Constants.BASE_URL}bouquets-tags/bouquet/${bouquetId}"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в список объектов TagDTO
                gson.fromJson(response.data, Array<BouquetTagDTO>::class.java).toList()
            }

            else -> emptyList()
        }
    }

    suspend fun findFlowersByBouquetId(bouquetId: Int): List<BouquetFlowerDTO> {
        val url = "${Constants.BASE_URL}bouquets-flowers/bouquet/${bouquetId}"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                gson.fromJson(response.data, Array<BouquetFlowerDTO>::class.java).toList()
            }

            else -> emptyList()
        }
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

        binder.registerCustomEditor(
            List::class.java,
            "flowers",
            object : PropertyEditorSupport() {
                override fun setAsText(text: String) {
                    runBlocking {
                        val flowerIds = text.split(",").mapNotNull { it.toIntOrNull() }
                        val flowers = getFlowersByIds(flowerIds)
                        value = flowers
                    }
                }
            }
        )
    }

    private suspend fun getFlowersByIds(flowerIds: List<Int>): List<FlowerDTO> {
        return getFlowers().filter { tag -> flowerIds.contains(tag.id) }
    }

}
