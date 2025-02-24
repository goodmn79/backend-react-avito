package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema(description = "список объявлений")
    private List<Ad> result;
}
