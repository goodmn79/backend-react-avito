package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final UserService userService;
    private final Validatable validator;

    public AdEntity map(CreateOrUpdateAd createOrUpdateAd) {
        return new AdEntity()
                .setPrice(validator.validate(createOrUpdateAd.getPrice()))
                .setTitle(validator.validate(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(validator.validate(createOrUpdateAd.getDescription(), 8, 64))
                .setAuthor(userService.getCurrentUser());
    }

    public Ad map(AdEntity adEntity) {
        return new Ad()
                .setPk(adEntity.getPk())
                .setImage(adEntity.getImage().getPath())
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor().getId());
    }

    public ExtendedAd mapExtendedAd (AdEntity adEntity) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setAuthorFirstName(adEntity.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getAuthor().getLastName());
        extendedAd.setEmail(adEntity.getAuthor().getEmail());
        extendedAd.setPhone(adEntity.getAuthor().getPhone());
        extendedAd.setPk(adEntity.getPk());
        extendedAd.setImage(adEntity.getImage().getPath());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        extendedAd.setAuthor(adEntity.getAuthor().getId());
        return extendedAd;
    }

    public List<Ad> map(List<AdEntity> adEntities) {
        return adEntities
                .stream()
                .map(this::map)
                .collect(Collectors.toUnmodifiableList());
    }
}
