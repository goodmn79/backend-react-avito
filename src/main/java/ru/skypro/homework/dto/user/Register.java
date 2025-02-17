package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Register {

    @Schema(type = "string", description = "логин", minLength = 4, maxLength = 32)
    private String username;

    @Schema(type = "string", description = "пароль", minLength = 8, maxLength = 16)
    private String password;

    @Schema(type = "string", description = "имя пользователя", minLength = 2, maxLength = 16)
    private String firstName;

    @Schema(type = "string", description = "фамилия пользователя", minLength = 2, maxLength = 16)
    private String lastName;

    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(type = "string", description = "роль пользователя", allowableValues = {"USER", "ADMIN"})
    private Role role;
}
