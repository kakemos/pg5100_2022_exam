package com.example.pg5100_exam.repo

import com.example.pg5100_exam.model.AnimalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimalRepo : JpaRepository<AnimalEntity, Long> {

    fun getByName(name: String): AnimalEntity
    fun getAnimalById(id: Long): AnimalEntity?
    fun save(animal: AnimalEntity?): AnimalEntity

}