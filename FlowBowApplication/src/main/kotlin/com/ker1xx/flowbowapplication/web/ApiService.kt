package com.ker1xx.flowbowapplication.web

import com.ker1xx.flowbowapplication.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.cglib.proxy.Dispatcher
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class ApiService(private val restTemplate: RestTemplate) {

    suspend fun sendGetRequestToApi(url: String, headers: Map<String, String>?): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val httpHeaders = HttpHeaders()
                headers?.forEach { (key, value) -> httpHeaders.set(key, value) }

                val entity = HttpEntity<Any>(httpHeaders)
                val response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String::class.java)
                Resource.Success(response.body ?: "No Content")
            } catch (e: HttpClientErrorException) {
                Resource.Error(
                    e.message ?: ("Unknown HttpClientErrorException error. " +
                            "${e.statusCode} ${e.message}")
                )
            } catch (e: Exception) {
                Resource.Error(
                    e.message ?: ("Unknown error" +
                            " ${e.message}")
                )
            }
        }
    }

    suspend fun <T> sendPutRequestToApi(url: String, requestBody: T, headers: Map<String, String>?): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val httpHeaders = HttpHeaders()
                headers?.forEach { (key, value) -> httpHeaders.set(key, value) }

                val entity = HttpEntity(requestBody, httpHeaders)
                restTemplate.put(url, entity)
                Resource.Success("Successfully updated")
            } catch (e: HttpClientErrorException) {
                Resource.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun sendDeleteRequestToApi(url: String, headers: Map<String, String>?): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val httpHeaders = HttpHeaders()
                headers?.forEach { (key, value) -> httpHeaders.set(key, value) }

                val entity = HttpEntity<Any>(httpHeaders)
                restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, entity, String::class.java)
                Resource.Success("Successfully deleted")
            } catch (e: HttpClientErrorException) {
                Resource.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun <T> sendPostRequestToApi(url: String, requestBody: T, headers: Map<String, String>?): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Создаём HttpHeaders и добавляем в него переданные заголовки
                val httpHeaders = HttpHeaders()
                headers?.forEach { (key, value) ->
                    httpHeaders.set(key, value)
                }

                // Создаём HttpEntity с телом запроса и заголовками
                val entity = HttpEntity(requestBody, httpHeaders)

                // Выполняем запрос с HttpEntity
                val response = restTemplate.postForObject(url, entity, String::class.java)
                Resource.Success(response ?: "No content")
            } catch (e: HttpClientErrorException) {
                Resource.Error(e.message ?: "Unknown error")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

}