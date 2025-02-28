package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.AdMapper;
import ru.skypro.homework.component.validation.Validatable;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final Validatable validator;
    private final ImageService imageService;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(AdService.class);

    public Ads getAllAds() {
        log.info("Получение всех объявлений.");

        List<AdEntity> ads = adRepository.findAll();
        log.info("Объявления успешно получены.");

        return adMapper.map(ads);
    }

    @PreAuthorize("isAuthenticated()")
    public Ad addAd(MultipartFile image, CreateOrUpdateAd createOrUpdateAd) throws IOException {
        log.info("Создание объявления.");
        AdEntity entity = adMapper.map(createOrUpdateAd);

        entity.setAuthor(userService.getCurrentUser());

        Image savedImage = imageService.saveImage(image, entity.getPk());

        log.debug("Сохранение объявления в базе данных.");
        AdEntity ad = adRepository.save(entity.setImage(savedImage));

        log.info("Объявление успешно создано.");
        return adMapper.map(ad);
    }

    public ExtendedAd getAdById(int pk) {
        log.info("Запрос на получение полного описания объявления.");

        AdEntity adEntity = this.getAdEntity(pk);

        log.info("Полное описание объявления успешно получено.");
        return adMapper.map(adEntity);
    }

    @PreAuthorize("hasRole('ADMIN') or @adService.isAdAuthor(#pk)")
    public void removeAdById(int pk) {
        log.warn("Удаление объявления.");

        AdEntity entity = this.getAdEntity(pk);

        adRepository.deleteById(entity.getPk());
        log.info("Объявление успешно удалено");
    }

    @PreAuthorize("@adService.isAdAuthor(#pk)")
    public Ad updateAdById(int pk, CreateOrUpdateAd createOrUpdateAd) {
        log.info("Запрос на обновление объявления.");
        AdEntity adEntity = this.getAdEntity(pk)
                .setTitle(
                        validator.validatedData(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(
                        validator.validatedData(createOrUpdateAd.getDescription(), 8, 64))
                .setPrice(
                        validator.validatedData(createOrUpdateAd.getPrice()));

        log.debug("Сохранение изменений объявления в базе данных.");
        AdEntity updatedEntity = adRepository.save(adEntity);

        log.info("Объявление успешно обновлено.");
        return adMapper.map(updatedEntity);
    }

    @PreAuthorize("@adService.isAdAuthor(#pk)")
    public byte[] updateImage(int pk, MultipartFile image) throws IOException {
        log.info("Изменение изображения объявления.");
        AdEntity entity = this.getAdEntity(pk);

        Image adImage = imageService.saveImage(image, entity.getPk());
        entity.setImage(adImage);

        log.debug("Сохранение нового изображения объявления в базе данных.");
        adRepository.save(entity);

        log.info("Изображение для объявления успешно обновлено.");
        return adImage.getData();
    }

    @PreAuthorize("isAuthenticated()")
    public Ads getAds() {
        log.info("Запрос на получение объявлений авторизованного пользователя.");
        UserEntity currentUser = userService.getCurrentUser();

        List<AdEntity> userAds = adRepository.findByAuthor(currentUser);
        Ads adsMe = adMapper.map(userAds);

        log.info("Объявления авторизованного пользователя успешно получены.");
        return adsMe;
    }

    public AdEntity getAdEntity(int pk) {
        return
                adRepository.findById(pk)
                        .orElseThrow(() -> {
                            log.error("Объявление не найдено!");
                            return new AdNotFoundException();
                        });
    }

    public boolean isAdAuthor(int pk) {
        int adAuthorId = this.getAdEntity(pk).getAuthor().getId();
        int currentUserId = userService.getCurrentUser().getId();
        return adAuthorId == currentUserId;
    }
}
