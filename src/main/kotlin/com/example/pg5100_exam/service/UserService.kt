package com.example.pg5100_exam.service

import com.example.pg5100_exam.controller.NewUserInfo
import com.example.pg5100_exam.model.AuthorityEntity
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.repo.AuthorityRepo
import com.example.pg5100_exam.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepo: UserRepo,
    @Autowired private val authorityRepo: AuthorityRepo
) : UserDetailsService {

    fun getAuthorities(): List<AuthorityEntity> {
        return authorityRepo.findAll()
    }

    fun getAuthority(name: String): AuthorityEntity {
        return authorityRepo.getByAuthorityName(name)
    }

    fun registerUser(newUserInfo: NewUserInfo): UserEntity {
        val newUser =
            UserEntity(email = newUserInfo.email, password = BCryptPasswordEncoder().encode(newUserInfo.password))
        newUser.authorities.add(getAuthority("USER"))
        return userRepo.save(newUser)
    }

    fun getUsers(): List<UserEntity> {
        return userRepo.findAll()
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userRepo.findByEmail(email)
    }

    fun getUserById(id: Long): UserEntity? {
        return userRepo.getById(id)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let { it ->
            val user = userRepo.findByEmail(it)
            return User(
                user?.email,
                user?.password,
                user?.authorities?.map { SimpleGrantedAuthority(it.authorityName) })
        }
        throw Exception("Error retrieving user!")
    }
}