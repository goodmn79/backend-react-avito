package ru.skypro.homework.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExtendedAd extends Ad {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String phone;
}
