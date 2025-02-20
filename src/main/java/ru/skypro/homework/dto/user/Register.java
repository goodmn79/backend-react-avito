package ru.skypro.homework.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.enums.Role;

@Data
@Accessors(chain = true)
public class Register {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
