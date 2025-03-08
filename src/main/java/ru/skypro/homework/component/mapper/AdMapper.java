package ru.skypro.homework.component.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Маппер для преобразования данных объявлений между DTO и сущностями.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class AdMapper {
    private final UserServiceImpl userServiceImpl;

    /**
     * Преобразует {@link CreateOrUpdateAd} в {@link AdEntity}.
     *
     * @param createOrUpdateAd DTO с данными для создания и обновления объявления, которое нужно преобразовать
     * @return {@link AdEntity}, сущность содержащую краткую информацию о товаре в объявлении
     */
    public AdEntity map(CreateOrUpdateAd createOrUpdateAd) {
        return new AdEntity()
                .setPrice(DataValidator.validatedPrice(createOrUpdateAd.getPrice()))
                .setTitle(DataValidator.validatedData(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(DataValidator.validatedData(createOrUpdateAd.getDescription(), 8, 64))
                .setAuthor(userServiceImpl.getCurrentUser());
    }

    /**
     * Преобразует {@link AdEntity} в {@link ExtendedAd}.
     *
     * @param adEntity Сущность объявления, которую нужно преобразовать
     * @return {@link ExtendedAd}, объект DTO содержащий полную информацию о товаре в объявлении
     */
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

    /**
     * Преобразует список {@link AdEntity} в {@link Ads}.
     *
     * @param adEntities Список объявлений, который нужно преобразовать
     * @return {@link Ads}, объект DTO содержащий информацию об общем количестве объявлений и список всех объявлений
     */
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
