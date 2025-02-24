package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ad {

    @Schema(description = "id автора объявления")
    private long author;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    private long pk;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "заголовок объявления")
    private String title;
}
