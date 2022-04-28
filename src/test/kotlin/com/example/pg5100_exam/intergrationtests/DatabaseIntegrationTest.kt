package com.example.pg5100_exam.intergrationtests

import com.example.pg5100_exam.controller.NewAnimalInfo
import com.example.pg5100_exam.controller.NewUserInfo
import com.example.pg5100_exam.model.AuthorityEntity
import com.example.pg5100_exam.model.Gender
import com.example.pg5100_exam.service.AnimalService
import com.example.pg5100_exam.service.UserService
import com.sun.istack.NotNull
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(UserService::class, AnimalService::class)
class DatabaseIntegrationTest(
    @Autowired private val animalService: AnimalService,
    @Autowired private val userService: UserService
) {
    @Test
    @DirtiesContext
    fun shouldGetUsers() {
        val result = userService.getUsers()
        assert(result.size == 2)
    }

    @Test
    @DirtiesContext
    fun registerAndFindUser() {
        val newUserInfo = NewUserInfo("ted@ted.ted", "ted")
        val createUser = userService.registerUser(newUserInfo)
        assert(createUser.email == "ted@ted.ted")

        val foundUser = userService.loadUserByUsername("ted@ted.ted")
        assert(createUser.email == foundUser.username)
    }

    @Test
    @DirtiesContext
    fun createUserThenGetByEmail() {
        val newUserInfo = NewUserInfo("george@ted.ted", "george")
        val createdUser = userService.registerUser(newUserInfo)
        assert(createdUser.email == "george@ted.ted")

        val foundUser = userService.getUserByEmail("george@ted.ted")
        assert(foundUser?.email == "george@ted.ted")
    }

    @Test
    @DirtiesContext
    fun createUserAndMakeSureAuthorityIsSetToUserByDefault() {
        val newUserInfo = NewUserInfo("george@ted.ted", "george")
        val createdUser = userService.registerUser(newUserInfo)
        val authorityUser = userService.getAuthority("USER")

        assert(createdUser.authorities.contains(authorityUser))
    }

    @Test
    @DirtiesContext
    fun shouldGetAnimals() {
        val result = animalService.getAnimals()
        assert(result.size == 5)
    }

    @Test
    @DirtiesContext
    fun shouldAddAndRetrieveAnimal() {
        val newAnimalInfo = NewAnimalInfo("Pepsi", "rabbit", "unknown", Gender.female, 3, false, null)
        val createAnimal = animalService.registerAnimal(newAnimalInfo)
        assert(createAnimal.name == "Pepsi")

        val foundAnimal = animalService.getAnimalByName("Pepsi")
        assert(foundAnimal.name == "Pepsi")
    }

    @Test
    @DirtiesContext
    fun shouldUpdateAnimal() {
        val animal = animalService.loadAnimalById(1)
        val newAnimalInfo = NewAnimalInfo("Pepsi", "rabbit", "unknown", Gender.female, 3, false, null)
        val updatedAnimal = animal.id?.let { animalService.updateAnimal(it, newAnimalInfo) }

        assert(updatedAnimal?.name == "Pepsi")
    }

    @Test
    @DirtiesContext
    fun shouldAdoptAnimal() {
        val animal = animalService.loadAnimalById(1)
        val owner = userService.getUserById(2)
        val adoptedAnimal = animal.id?.let { owner?.id?.let { it1 -> animalService.markAsAdopted(it, it1) } }

        assert(adoptedAnimal?.adopted == true)
        assert(adoptedAnimal?.owner == owner)
    }

    /*
    @Test
    fun shouldDeleteAnimal() {
        val animal = animalService.loadAnimalById(1)
        animal.id?.let { animalService.deleteAnimal(it) }
        assert(animal.id == (-1).toLong())
    }
    */

}