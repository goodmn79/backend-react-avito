package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class Comments {
    private int count;
    List<Comment> results;
}
