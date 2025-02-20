package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.component.Validatable;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final Validatable validator;

    // Преобразование из AdEntity в Ad (DTO)
    public Ad map(AdEntity adEntity) {
        return new Ad()
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor());
    }

    // Преобразование из CreateOrUpdateAd (DTO) в AdEntity
    public AdEntity map(CreateOrUpdateAd createOrUpdateAd) {
        return new AdEntity()
                .setPrice(validator.validate(createOrUpdateAd.getPrice()))
                .setTitle(validator.validate(createOrUpdateAd.getTitle(), 4, 32).toLowerCase())
                .setDescription(validator.validate(createOrUpdateAd.getDescription(), 8, 64).toLowerCase());
    }
}
