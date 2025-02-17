package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.skypro.homework.dto.ad.Ad;

import java.util.List;

@Data
@Accessors(chain = true)
public class Comments {

    @Schema(type = "integer", format = "int32", description = "общее количество комментариев")
    private int count;

    @Schema(type = "array", implementation = Comment.class)
    private List<Comment> results;
}
