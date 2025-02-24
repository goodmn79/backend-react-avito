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
        return new ExtendedAd()
                .setDescription(adEntity.getDescription())
                .setAuthorFirstName(adEntity.getAuthor().getFirstName())
                .setAuthorLastName(adEntity.getAuthor().getLastName())
                .setEmail(adEntity.getAuthor().getEmail())
                .setPhone(adEntity.getAuthor().getPhone())
                .setPk(adEntity.getPk())
                .setImage(adEntity.getImage().getPath())
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor().getId());
    }

    public List<Ad> map(List<AdEntity> adEntities) {
        return adEntities
                .stream()
                .map(this::map)
                .collect(Collectors.toUnmodifiableList());
    }


}
