package com.example.pg5100_exam.controller

import com.example.pg5100_exam.model.AuthorityEntity
import com.example.pg5100_exam.model.UserEntity
import com.example.pg5100_exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class AuthController(
    @Autowired private val userService: UserService
) {

    @GetMapping("/authentication/all")
    fun getAuthorities(): ResponseEntity<List<AuthorityEntity>> {
        return ResponseEntity.ok().body(userService.getAuthorities())
    }

    @GetMapping("/user/all")
    fun getUsers(): ResponseEntity<List<UserEntity>> {
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long?): ResponseEntity<UserEntity> {
        return if (id != null) {
            ResponseEntity.ok().body(id.let { userService.getUserById(it) })
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody newUserInfo: NewUserInfo): ResponseEntity<UserEntity> {
        val createdUser = userService.registerUser(newUserInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/register/authority").toUriString()
        )
        return ResponseEntity.created(uri).body(createdUser)
    }
}

data class NewUserInfo(val email: String, val password: String)