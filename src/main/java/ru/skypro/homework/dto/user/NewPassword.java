package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewPassword {

    @Schema(type = "string", description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @Schema(type = "string", description = "новый пароль", minLength = 8, maxLength = 16)
    private String newPassword;
}
