package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.enums.Role;

/**
 * DTO класс для передачи данных пользователя.
 * <p>
 * Содержит полную информацию о пользователе.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class User {

    @Schema(description = "id пользователя")
    private int id;

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
