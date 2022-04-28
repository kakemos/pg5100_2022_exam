package com.example.pg5100_exam.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

enum class Gender {
    female,
    male
}

@Entity
@Table(name = "animals")
class AnimalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animals_animal_id_seq")
    @SequenceGenerator(
        name = "animals_animal_id_seq",
        allocationSize = 1
    )
    @Column(name = "animal_id")
    val id: Long? = null,

    @Column(name = "animal_name")
    var name: String,

    @Column(name = "animal_type")
    var type: String,

    @Column(name = "animal_breed")
    var breed: String,

    @Column(name = "animal_gender")
    var gender: Gender,

    @Column(name = "animal_age")
    var age: Int,

    @Column(name = "animal_adopted")
    var adopted: Boolean? = false,

    @Column(name = "animal_created")
    val created: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "animal_enabled")
    var enabled: Boolean = true,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var owner: UserEntity? = null
) {
    override fun toString(): String {
        return "AnimalEntity(id=$id, name='$name', type='$type', breed='$breed', gender=$gender, age=$age, adopted=$adopted, created=$created, enabled=$enabled, owner=$owner)"
    }
}