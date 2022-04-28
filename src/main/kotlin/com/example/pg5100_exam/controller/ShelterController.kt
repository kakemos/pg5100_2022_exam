package com.example.pg5100_exam.controller

import com.example.pg5100_exam.model.AnimalEntity
import com.example.pg5100_exam.model.Gender
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.service.AnimalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class ShelterController(@Autowired private val animalService: AnimalService) {

    @PostMapping("/shelter/register-animal")
    fun registerAnimal(@RequestBody newAnimalInfo: NewAnimalInfo): ResponseEntity<AnimalEntity> {
        val createdAnimal = animalService.registerAnimal(newAnimalInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/shelter/register-animal").toString()
        )
        return ResponseEntity.created(uri).body(createdAnimal)
    }

    @GetMapping("/shelter/all-animals")
    fun getAnimals(): ResponseEntity<List<AnimalEntity>> {
        return ResponseEntity.ok().body(animalService.getAnimals())
    }

    @GetMapping("/shelter/animal/{id}")
    fun getOneAnimalById(@PathVariable id: Long): ResponseEntity<AnimalEntity> {
        return ResponseEntity.ok().body(animalService.loadAnimalById(id))
    }

    @DeleteMapping("/shelter/animal/{id}")
    fun deleteAnimal(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok().body(animalService.deleteAnimal(id))
    }

    @PutMapping("/shelter/animal/{id}")
    fun updateAnimal(@PathVariable id: Long, @RequestBody newAnimalInfo: NewAnimalInfo): ResponseEntity<AnimalEntity> {
        val createdAnimal = animalService.updateAnimal(id, newAnimalInfo)
        return ResponseEntity.ok().body(createdAnimal)
    }

    @PutMapping("/shelter/animal/{id}/adopted")
    fun adoptAnimal(@PathVariable id: Long, @RequestBody idUser: Long): ResponseEntity<AnimalEntity> {
        val adoptedAnimal = animalService.markAsAdopted(id, idUser)
        return ResponseEntity.ok().body(adoptedAnimal)
    }
}

data class NewAnimalInfo(
    val name: String,
    val type: String,
    val breed: String,
    val gender: Gender,
    val age: Int,
    val adopted: Boolean?,
    val owner: UserEntity?
)