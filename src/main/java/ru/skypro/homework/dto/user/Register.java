package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.enums.Role;

/**
 * DTO класс для передачи данных пользователя.
 * <p>
 * Содержит информацию для регистрации пользователя.
 * </p>
 *
 * @author Powered by ©AYE.team, sazonovfm
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class Register {

    @Schema(description = "логин", minLength = 4, maxLength = 32)
    private String username;

    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    private String password;

    @Schema(description = "имя пользователя", minLength = 2, maxLength = 16)
    private String firstName;

    @Schema(description = "фамилия пользователя", minLength = 2, maxLength = 16)
    private String lastName;

    @Schema(description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "роль пользователя", allowableValues = {"USER", "ADMIN"})
    private Role role;
}
