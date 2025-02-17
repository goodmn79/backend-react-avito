package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Login {

    @Schema(description = "логин", type = "string", minLength = 4, maxLength = 32)
    private String username;

    @Schema(type = "string", description = "пароль", minLength = 8, maxLength = 16)
    private String password;
}
