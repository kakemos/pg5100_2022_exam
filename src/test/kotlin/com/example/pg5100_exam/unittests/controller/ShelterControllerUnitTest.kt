package com.example.pg5100_exam.unittests.controller

import com.example.pg5100_exam.controller.NewAnimalInfo
import com.example.pg5100_exam.model.AnimalEntity
import com.example.pg5100_exam.model.Gender
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.service.AnimalService
import com.example.pg5100_exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class ShelterControllerUnitTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun userService() = mockk<UserService>()

        @Bean
        fun animalService() = mockk<AnimalService>()
    }

    @Autowired
    private lateinit var animalService: AnimalService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val dog = AnimalEntity(name = "Cola", type = "dog", breed = "dachs", gender = Gender.female, age = 2)
    private val cat = AnimalEntity(name = "Fanta", type = "cat", breed = "normal cat", gender = Gender.male, age = 34)
    private val userBill = UserEntity(email = "bill@bob.com", password = "password")

    @Test
    fun shouldGetAllAnimals() {
        every { animalService.getAnimals() } answers {
            mutableListOf(dog, cat)
        }

        mockMvc.get("/api/shelter/all-animals") {
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
            .andExpect { jsonPath("$[0].name") { value(dog.name) } }
            .andExpect { jsonPath("$[1].name") { value(cat.name) } }
            .andExpect { jsonPath("$[2].name") { doesNotExist() } }
    }

    @Test
    fun shouldGetAnimalById() {
        every { animalService.loadAnimalById(any()) } answers {
            cat
        }

        mockMvc.get("/api/shelter/animal/0") {
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.name") { value(cat.name) } }
    }

    @Test
    fun shouldDeleteAnimal() {
        every { animalService.deleteAnimal(any()) } answers {
        }

        mockMvc.delete("/api/shelter/animal/2") {
        }
            .andExpect { status { isOk() } }
    }

    @Test
    fun shouldUpdateAnimal() {
        every { animalService.updateAnimal(any(), any()) } answers {
            val chicken = AnimalEntity(
                name = "Solo",
                type = "chicken",
                breed = "fluffy",
                gender = Gender.female,
                age = 2,
                adopted = true,
                owner = userBill
            )
            chicken
        }

        mockMvc.put("/api/shelter/animal/1") {
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Solo\"," +
                    "\"type\":\"chicken\"," +
                    "\"breed\":\"fluffy\"," +
                    "\"gender\": 0," +
                    "\"age\": 2," +
                    "\"adopted\":\"true\"}" +
                    "\"owner\": 2}"
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.name") { value("Solo") } }
            .andExpect { jsonPath("$.adopted") { value(true) } }
    }

    @Test
    fun shouldAdoptAnimal() {
        every { animalService.markAsAdopted(1, 3) } answers {
            val chicken = AnimalEntity(
                name = "Solo",
                type = "chicken",
                breed = "fluffy",
                gender = Gender.female,
                age = 2,
                adopted = true,
                owner = userBill
            )
            chicken
        }

        mockMvc.put("/api/shelter/animal/1/adopted") {
            contentType = APPLICATION_JSON
            content = 3
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.adopted") { value(true) } }
    }

    @Test
    fun shouldRegisterNewAnimal() {
        every { animalService.registerAnimal(any()) } answers {
            cat
        }

        mockMvc.post("/api/shelter/register-animal") {
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Fanta\"," +
                    "\"type\":\"cat\"," +
                    "\"breed\":\"normal cat\"," +
                    "\"gender\": 1," +
                    "\"age\": 34}"
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.name") { value("Fanta") } }
            .andExpect { jsonPath("$.adopted") { value(false) } }
    }
}