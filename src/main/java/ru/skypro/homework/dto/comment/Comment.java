package ru.skypro.homework.dto.comment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Comment {
    private int pk;
    private int author;
    private String authorImage;
    private String authorFirstName;
    private long createdAt;
    private String text;
}
