package ru.skypro.homework.dto.ad;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ExtendedAd extends Ad {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
}
