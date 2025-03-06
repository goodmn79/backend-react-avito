package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * DTO класс для передачи данных объявлений.
 * <p>
 * Содержит информацию о количестве объявлений и их список.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Accessors(chain = true)
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema(description = "список объявлений")
    private List<Ad> result;
}
