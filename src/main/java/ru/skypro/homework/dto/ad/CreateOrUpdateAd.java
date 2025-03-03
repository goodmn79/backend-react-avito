package ru.skypro.homework.dto.ad;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrUpdateAd {

    @JsonProperty("title")
    @Schema(description = "заголовок объявления", minLength = 4, maxLength = 32)
    private String title;

    @JsonProperty("price")
    @Schema(description = "цена объявления", minimum = "0", maximum = "10000000")
    private int price;

    @JsonProperty("description")
    @Schema(description = "описание объявления", minLength = 8, maxLength = 64)
    private String description;
}
