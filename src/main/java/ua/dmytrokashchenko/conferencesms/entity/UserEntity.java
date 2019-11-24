package ua.dmytrokashchenko.conferencesms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "*Please provide your name")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email") // uniq - need to add
    @NotEmpty(message = "*Please provide an email") //domain - перенести
    private String email;

    @Column(name = "password")
    @Length(min = 6, message = "*Your password must have at least 6 characters") // -- таке перенести
    @NotEmpty(message = "*Please provide your password")  // -- таке перенести
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEntity roleEntity;

}
