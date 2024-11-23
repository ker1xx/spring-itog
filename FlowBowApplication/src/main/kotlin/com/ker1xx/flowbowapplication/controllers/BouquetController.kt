package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.customEntities.BouquetWithTagDisplayEntity
import com.ker1xx.flowbowapplication.models.dto.BouquetDTO
import com.ker1xx.flowbowapplication.models.dto.BouquetFlowerDTO
import com.ker1xx.flowbowapplication.models.dto.BouquetTagDTO
import com.ker1xx.flowbowapplication.models.dto.TagDTO
import com.ker1xx.flowbowapplication.utility.Constants
import com.ker1xx.flowbowapplication.utility.Resource
import com.ker1xx.flowbowapplication.web.ApiService
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/bouquets")
class BouquetController(
    private val apiService: ApiService,
    private val gson: Gson
) {
    @GetMapping
    fun showBouquets(
        @RequestParam(value = "tag", required = false) tagId: Int?,
        @RequestParam(value = "page", defaultValue = "1") page: Int,
        model: Model
    ): String = runBlocking {
        val pageSize = 15
        val offset = (page - 1) * pageSize

        val bouquets = if (tagId != null) {
            getBouquetsByTag(tagId, offset, pageSize)
        } else {
            getBouquets(offset, pageSize)
        }

        val bouquetToDisplay = bouquets.map { bouquet ->
            val bouquetTags = findTagsByBouquetId(bouquet?.id!!)
            val flowersInBouquet = findFlowersByBouquetId(bouquet.id)
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

        val totalBouquets = if (tagId != null) {
            countBouquetsByTag(tagId)
        } else {
            countAllBouquets()
        }

        val totalPages = (totalBouquets + pageSize - 1) / pageSize

        model.addAttribute("bouquets", bouquetToDisplay)
        model.addAttribute("tags", getTags())
        model.addAttribute("selectedTag", tagId)
        model.addAttribute("currentPage", page)
        model.addAttribute("totalPages", totalPages)

        return@runBlocking "bouquetsList"
    }

    private suspend fun countBouquetsByTag(tagId: Int): Int {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}bouquets-tags/tag/${tagId}", null)
        return if (response is Resource.Success) {
            val allBouquets = gson.fromJson(response.data, Array<BouquetTagDTO>::class.java).toList().map { it.bouquet }
            allBouquets.size
        } else
            return 0
    }

    private suspend fun countAllBouquets(): Int {
        val url = "${Constants.BASE_URL}bouquets"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в список объектов BouquetDTO
                val bouquetArray = gson.fromJson(response.data, Array<BouquetDTO>::class.java)
                bouquetArray.size
            }

            else -> 0
        }
    }

    private suspend fun getTags(): List<TagDTO> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}tags", null)
        return if (response is Resource.Success) {
            gson.fromJson(response.data, Array<TagDTO>::class.java).toList()
        } else emptyList()
    }

    private suspend fun getBouquetsByTag(tagId: Int, offset: Int, pageSize: Int): List<BouquetDTO?> {
        val response = apiService.sendGetRequestToApi("${Constants.BASE_URL}bouquets-tags/tag/${tagId}", null)
        return if (response is Resource.Success) {
            val allBouquets = gson.fromJson(response.data, Array<BouquetTagDTO>::class.java).toList().map { it.bouquet }
            allBouquets.drop(offset).take(pageSize)
        } else
            return emptyList()
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

    private suspend fun getBouquets(offset: Int = 0, pageSize: Int = 10): List<BouquetDTO> {
        val url = "${Constants.BASE_URL}bouquets"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                // Преобразуем JSON-ответ в список объектов BouquetDTO
                val bouquetArray = gson.fromJson(response.data, Array<BouquetDTO>::class.java)
                bouquetArray.toList().drop(offset).take(pageSize)
            }

            else -> {
                emptyList<BouquetDTO>()
            }
        }
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
}