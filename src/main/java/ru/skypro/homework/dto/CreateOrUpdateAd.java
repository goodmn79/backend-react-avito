package ru.skypro.homework.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrUpdateAd {
    private String title;
    private int price;
    private String description;
}
