package com.example.pg5100_exam.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(
        name = "users_user_id_seq",
        allocationSize = 1
    )
    @Column(name = "user_id")
    val id: Long? = null,

    @Column(name = "user_email")
    val email: String,

    @Column(name = "user_password")
    val password: String,

    @Column(name = "user_created")
    val created: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "user_enabled")
    val enabled: Boolean = true,

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
        name = "users_authorities",
        joinColumns = [JoinColumn(name = "authority_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val authorities: MutableList<AuthorityEntity> = mutableListOf()

) {
    @OneToMany
    @JoinColumn(name = "user_id")
    val adoptedAnimals: MutableSet<AnimalEntity?> = mutableSetOf()

    override fun toString(): String {
        return "UserEntity(id=$id, email='$email', password='$password', created=$created, enabled=$enabled, authorities=$authorities, adoptedAnimals=$adoptedAnimals)"
    }

}