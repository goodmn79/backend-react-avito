package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DTO класс для передачи данных пользователя.
 * <p>
 * Содержит логин и пароль для авторизации пользователя.
 * </p>
 *
 * @author Powered by ©AYE.team, sazonovfm, skypro-backend
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class Login {

    @Schema(description = "логин", minLength = 4, maxLength = 32)
    private String username;

    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    private String password;
}
