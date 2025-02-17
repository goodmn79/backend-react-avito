package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Ads {

    @Schema(type = "integer", format = "int32", description = "общее количество объявлений")
    private int count;

    @Schema(type = "array", implementation = Ad.class)
    private List<Ad> result;
}
