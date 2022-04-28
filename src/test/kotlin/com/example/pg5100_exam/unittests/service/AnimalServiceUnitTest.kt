package com.example.pg5100_exam.unittests.service

import com.example.pg5100_exam.controller.NewAnimalInfo
import com.example.pg5100_exam.model.AnimalEntity
import com.example.pg5100_exam.model.Gender
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.repo.AnimalRepo
import com.example.pg5100_exam.repo.UserRepo
import com.example.pg5100_exam.service.AnimalService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class AnimalServiceUnitTest {

    private val userRepo = mockk<UserRepo>()
    private val animalRepo = mockk<AnimalRepo>()
    private val animalService = AnimalService(animalRepo, userRepo)

    private val dog = AnimalEntity(name = "Cola", type = "dog", breed = "dachs", gender = Gender.female, age = 2)
    private val cat = AnimalEntity(name = "Fanta", type = "cat", breed = "normal cat", gender = Gender.male, age = 34)
    private val userBob = UserEntity(email = "bob@bob.com", password = "password")

    @Test
    fun shouldRegisterNewAnimal() {
        every { animalRepo.save(any()) } answers {
            val dog = AnimalEntity(name = "Cola", type = "dog", breed = "dachs", gender = Gender.female, age = 2)
            dog
        }

        val newAnimalInfo = NewAnimalInfo("Cola", "dog", "dachs", Gender.female, 2, false, null)
        val animal = animalService.registerAnimal(newAnimalInfo)

        assert(animal.enabled)
        assert(animal.name == "Cola")
    }

    @Test
    fun shouldGetAnimals() {
        every { animalRepo.findAll() } answers {
            mutableListOf(cat, dog)
        }

        val animals = animalService.getAnimals()
        assert(animals.size == 2)
        assert(animals[0].type == "cat")
        assert(animals[1].type == "dog")
    }

    @Test
    fun shouldGetOneAnimalByName() {
        every { animalRepo.getByName(any()) } answers {
            cat
        }

        val animal = animalService.getAnimalByName("Fanta")
        assert(animal.enabled)
        assert(animal.name == "Fanta")
    }

    @Test
    fun shouldLoadAnimalById() {
        every { animalRepo.getById(any()) } answers {
            dog
        }

        val animal = animalService.loadAnimalById(0)
        assert(animal.enabled)
        assert(animal.name == "Cola")
    }

    @Test
    fun shouldUpdateAnimal() {
        val newAnimalInfo = NewAnimalInfo("Pepsi", "rabbit", "unknown", Gender.female, 3, false, null)
        every { animalRepo.getAnimalById(any()) } answers {
            dog
        }

        val updatedDog = AnimalEntity(name = "Pepsi", type = "dog", breed = "unknown", gender = Gender.female, age = 3)
        every { animalRepo.save(any()) } answers {
            updatedDog
        }

        val animal = animalService.updateAnimal(0, newAnimalInfo)
        assert(animal.name == "Pepsi")
    }

    /*
    @Test
    fun shouldDeleteAnimal() {
        every { animalRepo.deleteById(any()) } answers {
        }

        val animal = animalService.deleteAnimal(1)
        //assert(animal == emptyArray<>())
    }
    */

    @Test
    fun shouldAdoptAnimal() {
        every { animalRepo.getAnimalById(any()) } answers {
            cat
        }
        every { userRepo.getById(any()) } answers {
            userBob
        }

        val updatedCat = AnimalEntity(name = "Fanta", type = "cat", breed = "normal cat", gender = Gender.male, age = 34, adopted = true, owner = userBob)
        every { animalRepo.save(any()) } answers {
            updatedCat
        }

        val animal = animalService.markAsAdopted(1, 2)
        assert(animal.adopted == true)
        assert(animal.owner == userBob)
    }

}