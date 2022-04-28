package com.example.pg5100_exam.unittests.controller

import com.example.pg5100_exam.controller.NewUserInfo
import com.example.pg5100_exam.model.AuthorityEntity
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.service.AnimalService
import com.example.pg5100_exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.apache.catalina.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun userService() = mockk<UserService>()
        @Bean
        fun animalService() = mockk<AnimalService>()
    }

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var mockMvc: MockMvc

    private val newUserInfo = NewUserInfo(email = "bob@bob.com", password = BCryptPasswordEncoder().encode("pirate"))
    private val userBob = UserEntity(email = "bob@bob.com", password = BCryptPasswordEncoder().encode("password"))
    private val userBill = UserEntity(email = "bill@bob.com", password = BCryptPasswordEncoder().encode("password"))
    private val authorityUser = AuthorityEntity(authorityName = "USER")
    private val authorityAdmin = AuthorityEntity(authorityName = "ADMIN")

    @Test
    fun shouldGetAllUsers() {
        every { userService.getUsers() } answers {
            mutableListOf(userBob, userBill)
        }

        mockMvc.get("/api/user/all"){
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
            .andExpect { jsonPath("$[0].email") { value("bob@bob.com")} }
            .andExpect { jsonPath("$[1].email") { value("bill@bob.com")} }
            .andExpect { jsonPath("$[2].email") { doesNotExist()} }
    }

    @Test
    fun shouldGetOneUser() {
        every { userService.getUserById(0) } answers {
            userBob
        }

        mockMvc.get("/api/user/0") {
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.email") { value("bob@bob.com")} }
    }

    @Test
    fun shouldGetBadRequestIfUserDoesNotExist() {
        every { userService.getUserById(any()) } answers {
            null
        }

        mockMvc.get("/api/user/null") {
        }
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun shouldGetAuthorities() {
        every { userService.getAuthorities() } answers {
            mutableListOf(authorityUser, authorityAdmin)
        }

        mockMvc.get("/api/authentication/all") {
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$") { isArray() } }
            .andExpect { jsonPath("$[0].authorityName") { value(authorityUser.authorityName) } }
            .andExpect { jsonPath("$[1].authorityName") { value(authorityAdmin.authorityName) } }
    }

    @Test
    fun shouldRegisterUser() {
        every { userService.registerUser(any()) } answers {
            val user = UserEntity(id = 1, email = "bob@bob.com", password = BCryptPasswordEncoder().encode("password"))
            user
        }

        mockMvc.post("/api/register") {
            contentType = APPLICATION_JSON
            content = "{\"email\":\"bob@bob.com\"," +
                    "\"password\":\"password\"}"
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { jsonPath("$.email") { value("bob@bob.com") } }
    }


}