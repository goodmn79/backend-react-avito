package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUser {

    @Schema(type = "string", description = "имя пользователя", minLength = 3, maxLength = 10)
    private String firstName;

    @Schema(type = "string", description = "фамилия пользователя", minLength = 3, maxLength = 10)
    private String lastName;

    @Schema(type = "string", description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}
