package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DTO класс для передачи данных пользователя.
 * <p>
 * Содержит информацию о текущем и новом пароле пользователя.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class NewPassword {

    @Schema(description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @Schema(description = "новый пароль", minLength = 8, maxLength = 16)
    private String newPassword;
}
