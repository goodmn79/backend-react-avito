package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.validate.DataValidator;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final DataValidator validator;

    // Преобразование из AdEntity в Ad (DTO)
    public Ad toAd(AdEntity adEntity) {
        Ad ad = new Ad();
        return ad.setPk(adEntity.getPk())
                .setImage(adEntity.getImage())
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor());
    }

    // Преобразование из Ad (DTO) в AdEntity
    public AdEntity toAdEntity(Ad ad) {

        validator.validateStringLengthRange(32, 4, ad.getTitle());
        validator.validateStringLengthRange(64, 8, ad.getDescription());
        validator.validatePrice(ad.getPrice());
        validator.validatePhone(ad.getPhone());

        return new AdEntity()
                .setImage(ad.getImage())
                .setPk(ad.getPk())
                .setPrice(ad.getPrice())
                .setTitle(ad.getTitle())
                .setDescription(ad.getDescription())
                .setAuthor(new UserEntity().setId(ad.getAuthor()));
    }

}
