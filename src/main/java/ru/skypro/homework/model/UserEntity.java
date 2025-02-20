package ru.skypro.homework.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.skypro.homework.enums.Role;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone; //\+7\s?\(?\d{3}\)?\s?\d{3}-?\d{2}-?\d{2}
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Image image;
}
