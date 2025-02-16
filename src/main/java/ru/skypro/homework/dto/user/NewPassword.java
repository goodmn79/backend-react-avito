package ru.skypro.homework.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewPassword {
    private String currentPassword;
    private String newPassword;
}
