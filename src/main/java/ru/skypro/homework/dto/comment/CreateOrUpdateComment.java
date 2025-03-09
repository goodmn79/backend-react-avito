package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DTO класс для передачи данных комментария.
 * <p>
 * Содержит текст комментария для его добавления или обновления.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class CreateOrUpdateComment {
    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
