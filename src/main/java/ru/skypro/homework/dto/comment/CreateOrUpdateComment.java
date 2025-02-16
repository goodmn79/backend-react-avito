package ru.skypro.homework.dto.comment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateOrUpdateComment {
    private String text;
}
