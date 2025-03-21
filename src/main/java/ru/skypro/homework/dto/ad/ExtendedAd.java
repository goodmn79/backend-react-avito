package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * DTO класс для передачи данных объявления.
 * <p>
 * Содержит полную информацию об объявлении.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ExtendedAd extends Ad {

    @Schema(description = "описание объявления")
    private String description;

    @Schema(description = "имя автора объявления")
    private String authorFirstName;

    @Schema(description = "фамилия автора объявления")
    private String authorLastName;

    @Schema(description = "логин автора объявления")
    private String email;

    @Schema(description = "телефон автора объявления")
    private String phone;

    @Override
    public ExtendedAd setPk(int pk) {
        super.setPk(pk);
        return this;
    }

    @Override
    public ExtendedAd setImage(String image) {
        super.setImage(image);
        return this;
    }

    @Override
    public ExtendedAd setPrice(int price) {
        super.setPrice(price);
        return this;
    }

    @Override
    public ExtendedAd setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public ExtendedAd setAuthor(int id) {
        super.setAuthor(id);
        return this;
    }
}
