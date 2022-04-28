package com.example.pg5100_exam.repo

import com.example.pg5100_exam.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserEntity, Long> {

    fun findByEmail(email: String): UserEntity?

}