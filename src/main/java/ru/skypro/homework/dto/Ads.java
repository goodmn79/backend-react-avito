package ru.skypro.homework.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Ads {
    private int count;
    private List<Ad> result;
}
