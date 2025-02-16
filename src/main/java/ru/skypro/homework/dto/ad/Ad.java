package ru.skypro.homework.dto.ad;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Ad {
    private long author;
    private String image;
    private long pk;
    private int price;
    private String title;
}
