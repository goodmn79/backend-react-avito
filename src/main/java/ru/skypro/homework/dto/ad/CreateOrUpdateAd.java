package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrUpdateAd {

    @Schema(type = "string", description = "заголовок объявления", minLength = 4, maxLength = 32)
    private String title;

    @Schema(type = "integer", format = "int32", description = "цена объявления", minimum = "0", maximum = "10000000")
    private int price;

    @Schema(type = "string", description = "описание объявления", minLength = 8, maxLength = 64)
    private String description;
}
