package com.example.pg5100_exam.intergrationtests

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FullSystemTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldGetAllAuthoritiesIntegrationTest() {

        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/user/all") {
            theCookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
    }

    @Test
    fun shouldLetAdminAddAnAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/register-animal") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Fanta\"," +
                    "\"type\":\"cat\"," +
                    "\"breed\":\"normal cat\"," +
                    "\"gender\": 1," +
                    "\"age\": 34}"
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
    }

    @Test
    fun shouldLetAdminLogInAndAddNewAnimalBeforeViewingNewAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/register-animal") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Fanta\"," +
                    "\"type\":\"cat\"," +
                    "\"breed\":\"normal cat\"," +
                    "\"gender\": 1," +
                    "\"age\": 34}"
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        mockMvc.get("/api/shelter/animal/6") {
            theCookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.name") { value("Fanta") } }
    }

    @Test
    fun shouldLetAdminDeleteAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.delete("/api/shelter/animal/4") {
            theCookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
    }

    @Test
    fun shouldLetAdminUpdateAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/animal/4") {
            theCookie?.let { cookie(it) }
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
            .andExpect { jsonPath("$.breed") { value("fluffy") } }
    }

    @Test
    fun shouldLetAdminRegisterAnimalAsAdopted(){
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"admin@admin.com\",\n" +
                    "    \"password\": \"admin\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/animal/1/adopted") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = 2
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.name") { value("Rufus") } }
            .andExpect { jsonPath("$.adopted") { value(true) } }
    }

    @Test
    fun shouldLetNormalUserSeeCuteAnimals() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"user@user.com\",\n" +
                    "    \"password\": \"user\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.get("/api/shelter/all-animals") {
            theCookie?.let { cookie(it) }
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
    }

    @Test
    fun shouldNotAddAnimalWithoutLoggingIn() {
        mockMvc.post("/api/shelter/register-animal") {
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Fanta\"," +
                    "\"type\":\"cat\"," +
                    "\"breed\":\"normal cat\"," +
                    "\"gender\": 1," +
                    "\"age\": 34}"
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldNotLetNormalUserAddAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"user@user.com\",\n" +
                    "    \"password\": \"user\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.post("/api/shelter/register-animal") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Fanta\"," +
                    "\"type\":\"cat\"," +
                    "\"breed\":\"normal cat\"," +
                    "\"gender\": 1," +
                    "\"age\": 34}"
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldNotLetNormalUserDeleteAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"user@user.com\",\n" +
                    "    \"password\": \"user\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.delete("/api/shelter/animal/4") {
            theCookie?.let { cookie(it) }
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldNotLetNormalUserUpdateAnimal() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"user@user.com\",\n" +
                    "    \"password\": \"user\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/animal/4") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = "{\"name\":\"Solo\"," +
                    "\"type\":\"chicken\"," +
                    "\"breed\":\"fluffy\"," +
                    "\"gender\": 0," +
                    "\"age\": 2," +
                    "\"adopted\":\"true\"}" +
                    "\"owner\": 2}"
        }
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun shouldNotLetNormalUserRegisterAnimalAsAdopted() {
        val loggedInUser = mockMvc.post("/api/login") {
            contentType = APPLICATION_JSON
            content = "{\n" +
                    "    \"email\": \"user@user.com\",\n" +
                    "    \"password\": \"user\"}"
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val theCookie = loggedInUser.response.getCookie("access_token")

        mockMvc.put("/api/shelter/animal/1/adopted") {
            theCookie?.let { cookie(it) }
            contentType = APPLICATION_JSON
            content = 2
        }
            .andExpect { status { isForbidden() } }
    }

}