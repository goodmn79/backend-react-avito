package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ad {

    @Schema(type = "integer", format = "int32", description = "id автора объявления")
    private long author;

    @Schema(type = "string", description = "ссылка на картинку объявления")
    private String image;

    @Schema(type = "integer", format = "int32", description = "id объявления")
    private long pk;

    @Schema(type = "integer", format = "int32", description = "цена объявления")
    private int price;

    @Schema(type = "string", description = "заголовок объявления")
    private String title;
}
