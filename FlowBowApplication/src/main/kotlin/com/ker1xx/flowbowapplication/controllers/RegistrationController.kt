package com.ker1xx.flowbowapplication.controllers

import com.google.gson.Gson
import com.ker1xx.flowbowapplication.models.dto.*
import com.ker1xx.flowbowapplication.models.helpers.Hashing
import com.ker1xx.flowbowapplication.utility.Constants
import com.ker1xx.flowbowapplication.utility.Resource
import com.ker1xx.flowbowapplication.web.ApiService
import jakarta.servlet.http.HttpSession
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Controller
class RegistrationController(
    private val apiService: ApiService
) {

    // Страница с формой для ввода логина и пароля
    @GetMapping("/registration")
    fun showRegistrationForm(model: Model): String {
        return "registration"  // HTML страница с формой
    }

    // POST запрос для обработки формы логина и пароля
    @PostMapping("/registration")
    fun handleRegistration(
        @RequestParam login: String,
        @RequestParam password: String,
        model: Model,
        session: HttpSession
    ): String {
        val hashingObject = Hashing()
        val saltString = hashingObject.generateRandomSalt()
        val hashedPassword = hashingObject.hashWithSalt(password, saltString)
        val authorizationData = AuthorizationDataDTO(
            id = null,
            login = login,
            password = hashedPassword,
            salt = saltString
        )
        val duplicateCheckResponse = runBlocking {
            checkForDuplicates(authorizationData)
        }

        if (password.length < 6) {
            model.addAttribute("error", "User already exists")
            return "registration"
        }

        return if (duplicateCheckResponse!!) {
            model.addAttribute("error", "User already exists")
            "registration"
        } else {
            session.setAttribute("login", login)
            session.setAttribute("password", hashedPassword)
            session.setAttribute("salt", saltString)
            "redirect:/registration/personalData"
        }
    }

    private suspend fun checkForDuplicates(authorizationDataDTO: AuthorizationDataDTO): Boolean? {
        val url = "${Constants.BASE_URL}authorization-data/check-for-duplicates"
        val response = apiService.sendPostRequestToApi(url, authorizationDataDTO, null)
        return when (response) {
            is Resource.Success -> response.data?.let { data ->
                try {
                    Gson().fromJson(data, CheckForDuplicateDTO::class.java).duplicate
                } catch (e: Exception) {
                    true
                }
            }

            else -> true
        }
    }

    // Страница для ввода персональных данных
    @GetMapping("/registration/personalData")
    fun showPersonalDataForm(model: Model): String {
        return "personalData"  // HTML страница с формой для ввода ФИО и телефона
    }

    // POST запрос для отправки персональных данных
    @PostMapping("/registration/personalData")
    fun handlePersonalData(
        @RequestParam name: String,
        @RequestParam lastname: String,
        @RequestParam patronymic: String,
        @RequestParam phone: String,
        model: Model,
        session: HttpSession
    ): String = runBlocking {
        val login = session.getAttribute("login") as? String
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing login in session")
        val password = session.getAttribute("password") as? String
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing password in session")
        val salt = session.getAttribute("salt") as? String
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing salt in session")
        val authorizationData = AuthorizationDataDTO(
            id = null,
            login = login,
            password = password,
            salt = salt
        )

        val registrationResponse = registerAuthorizationData(authorizationData)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register authorization data")

        val jwtToken = registrationResponse.token
        authorizationData.id = registrationResponse.id

        session.setAttribute("jwtToken", jwtToken)
        session.setAttribute("authorizationId", authorizationData.id)

        val personalData = PersonalDataDTO(
            id = null,
            name = name,
            lastname = lastname,
            patronymic = patronymic,
            phoneNumber = phone
        )

        val personalDataResult = sendPersonalData(jwtToken, personalData)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save personal data")

        val clientData = ClientDTO(
            id = null,
            authorizationData = authorizationData,
            personalData = personalDataResult
        )

        val clientSaved = saveClient(jwtToken, clientData)
        if (!clientSaved) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save client data")
        }

        "redirect:/registration/complete"
    }

    private suspend fun registerAuthorizationData(dto: AuthorizationDataDTO): RegistrationResponseDTO? {
        val url = "${Constants.BASE_URL}authorization-data/registration"
        val response = apiService.sendPostRequestToApi(url, dto, null)
        return when (response) {
            is Resource.Success -> {
                response.data?.let { data ->
                    try {
                        Gson().fromJson(data, RegistrationResponseDTO::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
            }

            else -> null
        }
    }

    private suspend fun sendPersonalData(jwtToken: String, dto: PersonalDataDTO): PersonalDataDTO? {
        val url = "${Constants.BASE_URL}personal-data"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendPostRequestToApi(url, dto, headers)
        return when (response) {
            is Resource.Success -> response.data?.let { data ->
                try {
                    Gson().fromJson(data, PersonalDataDTO::class.java)
                } catch (e: Exception) {
                    null
                }
            }

            else -> null
        }
    }

    private suspend fun saveClient(jwtToken: String, dto: ClientDTO): Boolean {
        val url = "${Constants.BASE_URL}clients"
        val headers = mapOf("Authorization" to "Bearer $jwtToken")
        val response = apiService.sendPostRequestToApi(url, dto, headers)
        return when (response) {
            is Resource.Success -> true
            else -> false
        }
    }

    @GetMapping("/registration/complete")
    fun showRegistrationComplete(model: Model): String {
        return "complete"  // Страница с успешным завершением регистрации
    }
}
