package ru.skypro.homework.dto.ad;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ad {
    private int pk;
    private String title;
    private String image;
    private int price;
    private int author;
}
