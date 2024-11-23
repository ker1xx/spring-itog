package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.dto.TagDTO
import com.ker1xx.flowbowapplication.utility.Constants
import com.ker1xx.flowbowapplication.web.ApiService
import com.ker1xx.flowbowapplication.utility.Resource
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import kotlinx.coroutines.runBlocking

@Controller
@RequestMapping("/admin/tags")
class TagController(private val apiService: ApiService, private val gson: Gson) {

    @GetMapping
    fun showTags(model: Model): String = runBlocking {
        val tags = getTags()
        model.addAttribute("tags", tags)
        return@runBlocking "admin/tags"
    }

    @GetMapping("/new")
    fun showCreateForm(model: Model): String {
        val tag = TagDTO(id = 0, name = "")
        model.addAttribute("tag", tag)
        return "admin/tag_create"
    }

    @PostMapping("/new")
    fun createTag(@ModelAttribute tag: TagDTO, session: HttpSession): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}tags"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendPostRequestToApi(url, tag, headers)

        if (response is Resource.Success) {
            return@runBlocking "redirect:/admin/tags"
        } else {
            return@runBlocking "admin/tag_create"
        }
    }

    @GetMapping("/{id}/edit")
    fun showEditForm(@PathVariable id: Int, model: Model): String = runBlocking {
        val tag = getTagById(id)
        model.addAttribute("tag", tag)
        return@runBlocking "admin/tag_edit"
    }

    @PostMapping("/{id}/edit")
    fun editTag(@PathVariable id: Int, @ModelAttribute tagDTO: TagDTO, session: HttpSession): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}tags/$id"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendPutRequestToApi(url, tagDTO, headers)

        if (response is Resource.Success) {
            return@runBlocking "redirect:/admin/tags"
        } else {
            return@runBlocking "admin/tag_edit"
        }
    }

    @GetMapping("/{id}/delete")
    fun deleteTag(@PathVariable id: Int, session: HttpSession): String = runBlocking {
        val jwtToken = session.getAttribute("jwtToken") as String?

        if (jwtToken.isNullOrEmpty()) {
            return@runBlocking "redirect:/login"
        }

        val url = "${Constants.BASE_URL}tags/$id"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendDeleteRequestToApi(url, headers)

        if (response is Resource.Success) {
            return@runBlocking "redirect:/admin/tags"
        } else {
            return@runBlocking "admin/tags"
        }
    }

    private suspend fun getTags(): List<TagDTO> {
        val url = "${Constants.BASE_URL}tags"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                val tagArray = gson.fromJson(response.data, Array<TagDTO>::class.java)
                tagArray.toList()
            }
            else -> emptyList()
        }
    }

    private suspend fun getTagById(id: Int): TagDTO? {
        val url = "${Constants.BASE_URL}tags/$id"
        val response = apiService.sendGetRequestToApi(url, null)

        return when (response) {
            is Resource.Success -> {
                gson.fromJson(response.data, TagDTO::class.java)
            }
            else -> null
        }
    }
}
