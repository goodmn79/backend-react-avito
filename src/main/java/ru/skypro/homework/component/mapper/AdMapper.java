package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
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

    public AdEntity map(CreateOrUpdateAd createOrUpdateAd) {
        return new AdEntity()
                .setPrice(DataValidator.validatedPrice(createOrUpdateAd.getPrice()))
                .setTitle(DataValidator.validatedData(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(DataValidator.validatedData(createOrUpdateAd.getDescription(), 8, 64))
                .setAuthor(userService.getCurrentUser());
    }

    public ExtendedAd map(AdEntity adEntity) {
        return new ExtendedAd()
                .setPk(adEntity.getPk())
                .setImage(adEntity.getImage().getPath())
                .setPrice(adEntity.getPrice())
                .setTitle(adEntity.getTitle())
                .setAuthor(adEntity.getAuthor().getId())
                .setDescription(adEntity.getDescription())
                .setAuthorFirstName(adEntity.getAuthor().getFirstName())
                .setAuthorLastName(adEntity.getAuthor().getLastName())
                .setEmail(adEntity.getAuthor().getEmail())
                .setPhone(adEntity.getAuthor().getPhone());
    }

    public Ads map(List<AdEntity> adEntities) {
        return new Ads()
                .setCount(adEntities.size())
                .setResult(adEntities
                        .stream()
                        .map(a -> new Ad()
                                .setPk(a.getPk())
                                .setTitle(a.getTitle())
                                .setPrice(a.getPrice())
                                .setAuthor(a.getAuthor().getId())
                                .setImage(a.getImage().getPath()))
                        .collect(Collectors.toUnmodifiableList()));
    }
}
