package com.example.pg5100_exam.unittests.service

import com.example.pg5100_exam.controller.NewUserInfo
import com.example.pg5100_exam.model.AuthorityEntity
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.repo.AuthorityRepo
import com.example.pg5100_exam.repo.UserRepo
import com.example.pg5100_exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class UserServiceUnitTest {

    private val userRepo = mockk<UserRepo>()
    private val authorityRepo = mockk<AuthorityRepo>()
    private val userService = UserService(userRepo, authorityRepo)

    val userJoe = UserEntity(email = "joe@bob.com", password = "pirate")
    val userJim = UserEntity(email = "jim@bob.com", password = "pirate")

    @Test
    fun shouldGetUsers() {
        // mocking out responses from findAll() in getUsers()
        every { userRepo.findAll() } answers {
            mutableListOf(userJoe, userJim)
        }

        val users = userService.getUsers()
        assert(users.size == 2)
        assert(users.first { it.email == "jim@bob.com" }.password == "pirate")
    }

    @Test
    fun shouldRegisterNewUser() {
        // mocking out responses from save() (doing this for all functions in registerUser())
        every { userRepo.save(any()) }  answers {
            // returns whatever we pass to it
            firstArg()
        }

        // mocking out responses from getByAuthorityName()
        every { authorityRepo.getByAuthorityName(any()) } answers {
            AuthorityEntity(authorityName = "USER")
        }

        val createUser = userService.registerUser(NewUserInfo("dumb@ass.com", "beaniebaby"))

        assert(createUser.email == "dumb@ass.com")
        assert(createUser.enabled)
    }

    @Test
    fun shouldGetAuthorities() {
        val userAuthority = AuthorityEntity(authorityName = "USER")
        val adminAuthority = AuthorityEntity(authorityName = "ADMIN")
        every { authorityRepo.findAll() } answers {
            mutableListOf(userAuthority, adminAuthority)
        }

        val authorities = userService.getAuthorities()
        assert(authorities.size == 2)
        assert(authorities[0] == userAuthority)
        assert(authorities[1] == adminAuthority)
    }

    @Test
    fun shouldGetAuthorityByName() {
        every { authorityRepo.getByAuthorityName("ADMIN") } answers {
            val adminAuthority = AuthorityEntity(authorityName = "ADMIN")
            adminAuthority
        }

        val authority = userService.getAuthority("ADMIN")
        assert(authority.authorityName == "ADMIN")
    }

    @Test
    fun shouldGetUserByEmail() {
        every { userRepo.findByEmail("jim@bob.com") } answers {
            userJim
        }

        val user = userService.getUserByEmail("jim@bob.com")
        assert(user?.enabled == true)
        assert(user?.email == "jim@bob.com")
    }

    @Test
    fun shouldGetUserById() {
        every { userRepo.getById(1) } answers {
            userJoe
        }

        var user = userService.getUserById(1)
        assert(user?.enabled == true)
        assert(user?.email == "joe@bob.com")
    }

    @Test
    fun shouldLoadUserByEmail() {
        every { userRepo.findByEmail("joe@bob.com") } answers {
            userJoe
        }

        val user = userService.getUserByEmail("joe@bob.com")
        assert(user?.enabled == true)
        assert(user?.email == "joe@bob.com")
    }

}