package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int count;

    @Schema(description = "список комментариев")
    private List<Comment> results;
}
