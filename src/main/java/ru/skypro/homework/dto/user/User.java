package ru.skypro.homework.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.enums.Role;

@Data
@Accessors(chain = true)
public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
