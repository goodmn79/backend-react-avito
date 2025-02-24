package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ad {

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "заголовок объявления")
    private String title;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "id автора объявления")
    private int author;
}
