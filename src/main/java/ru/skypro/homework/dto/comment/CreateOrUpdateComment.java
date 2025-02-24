package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CreateOrUpdateComment {
    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
}
