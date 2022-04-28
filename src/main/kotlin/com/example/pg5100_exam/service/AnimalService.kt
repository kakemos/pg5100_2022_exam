package com.example.pg5100_exam.service

import com.example.pg5100_exam.controller.NewAnimalInfo
import com.example.pg5100_exam.model.AnimalEntity
import com.example.pg5100_exam.repo.AnimalRepo
import com.example.pg5100_exam.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AnimalService(
    @Autowired private val animalRepo: AnimalRepo,
    @Autowired private val userRepo: UserRepo
) : UserDetailsService {

    fun registerAnimal(newAnimalInfo: NewAnimalInfo): AnimalEntity {
        val newAnimal =
            AnimalEntity(
                name = newAnimalInfo.name,
                type = newAnimalInfo.type,
                breed = newAnimalInfo.breed,
                gender = newAnimalInfo.gender,
                age = newAnimalInfo.age
            )
        return animalRepo.save(newAnimal)
    }

    fun getAnimals(): List<AnimalEntity> {
        return animalRepo.findAll()
    }

    fun getAnimalByName(name: String): AnimalEntity {
        return animalRepo.getByName(name)
    }

    fun loadAnimalById(id: Long): AnimalEntity {
        return animalRepo.getById(id)
    }

    fun updateAnimal(id: Long, newAnimalInfo: NewAnimalInfo): AnimalEntity {
        val animal = animalRepo.getAnimalById(id)

        animal?.name = newAnimalInfo.name
        animal?.type = newAnimalInfo.type
        animal?.breed = newAnimalInfo.breed
        animal?.age = newAnimalInfo.age
        animal?.gender = newAnimalInfo.gender
        animal?.adopted = newAnimalInfo.adopted
        animal?.owner = newAnimalInfo.owner

        return animalRepo.save(animal)
    }

    fun deleteAnimal(id: Long) {
        return animalRepo.deleteById(id)
    }

    fun markAsAdopted(id: Long, idUser: Long): AnimalEntity {
        val animal = animalRepo.getAnimalById(id)
        val owner = userRepo.getById(idUser)

        animal?.adopted = true
        animal?.owner = owner

        return animalRepo.save(animal)
    }

    // for some reason I had to include this in the class
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
    
    /*
    fun loadAnimalByName(name: String?): AnimalEntity {
        name?.let {
            val animal = animalRepo.findByName(it)
            return AnimalEntity(
                animal?.id,
                animal!!.name,
                animal.type,
                animal.breed,
                animal.gender,
                animal.age,
                animal.adopted,
                animal.created,
                animal.enabled,
                animal.owner
            )
        }
        throw Exception("Error retrieving animal!")
    }
    */

}
