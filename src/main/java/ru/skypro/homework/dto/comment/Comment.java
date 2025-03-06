package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DTO класс для передачи данных комментария.
 * <p>
 * Содержит информацию о комментарии.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class Comment {

    @Schema(description = "id комментария")
    private int pk;

    @Schema(description = "id автора комментария")
    private int author;

    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private long createdAt;

    @Schema(description = "текст комментария")
    private String text;
}
