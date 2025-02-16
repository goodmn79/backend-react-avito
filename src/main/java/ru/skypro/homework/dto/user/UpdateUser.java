package ru.skypro.homework.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUser {
    private String firstName;
    private String lastName;
    private String phone;
}
