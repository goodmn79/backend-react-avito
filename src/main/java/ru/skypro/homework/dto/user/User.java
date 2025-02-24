package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    @Schema(description = "id пользователя")
    private long id;

    @Schema(description = "логин пользователя")
    private String email;

    @Schema(description = "имя пользователя")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "роль пользователя", allowableValues = {"USER", "ADMIN"})
    private Role role;

    @Schema(description = "ссылка на аватар пользователя")
    private String image;
}
