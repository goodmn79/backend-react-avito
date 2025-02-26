package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String AD_NOT_FOUND = "Объявление не найдено";


    public Ads getAllAds() {
        log.info("Получение всех объявлений.");
        List<AdEntity> ads = adRepository.findAll();
        Ads allAds = new Ads().setCount(ads.size()).setResult(adMapper.map(ads));
        log.info("Объявления успешно получены.Количество: {}", allAds.getCount());
        return allAds;
    }

    public Ad addAd(MultipartFile image, CreateOrUpdateAd createOrUpdateAd) throws IOException {
        log.info("Создание объявления.");
        AdEntity adEntity = adMapper.map(createOrUpdateAd);
        Image savedImage = imageService.saveAdImage(image, adEntity);
        adEntity.setImage(savedImage);

        log.debug("Сохранение объявления в базе данных: {}", adEntity);
        adRepository.save(adEntity);

        log.info("Объявление успешно создано c id: {}", adEntity.getPk());
        return adMapper.map(adEntity);
    }

    public ExtendedAd getAdById(int pk) {
        log.info("Запрос на получение полного описания объявления по id {}.", pk);
        AdEntity adEntity = adRepository.findById(pk)
                .orElseThrow(() -> {
                    logAdNotFound(pk);
                    return new AdNotFoundException(AD_NOT_FOUND);
                });
        log.info("Полное описание объявления по id {} успешно получено.", pk);
        return adMapper.mapExtendedAd(adEntity);
    }

    public void removeAdById(int pk) {
        log.info("Запрос на удаление объявления по id: {}", pk);

        if (!adRepository.existsById(pk)) {
            logAdNotFound(pk);
            throw new AdNotFoundException(AD_NOT_FOUND);
        }

        adRepository.deleteById(pk);
        log.info("Объявление с id {} удалено", pk);
    }

    public Ad updateAdById(int pk, CreateOrUpdateAd createOrUpdateAd) {
        log.info("Запрос на обновление объявления с id {}.", pk);
        AdEntity adEntity = adRepository.findById(pk)
                .orElseThrow(() -> {
                    logAdNotFound(pk);
                    return new AdNotFoundException(AD_NOT_FOUND);
                });

        adEntity.setTitle(validator.validate(createOrUpdateAd.getTitle(), 4, 32));
        adEntity.setDescription(validator.validate(createOrUpdateAd.getDescription(), 8, 64));
        adEntity.setPrice(validator.validate(createOrUpdateAd.getPrice()));

        log.debug("Сохранение изменений объявления в базе данных: {}.", adEntity);
        AdEntity updatedEntity = adRepository.save(adEntity);

        log.info("Объявление c id {} успешно обновлено.", pk);
        return adMapper.map(updatedEntity);
    }


    public byte[] updateImage(int pk, MultipartFile image) throws IOException {
        log.info("Запрос на изменение изображения объявления с id {}", pk);
        AdEntity adEntity = adRepository.findById(pk)
                .orElseThrow(() -> {
                    logAdNotFound(pk);
                    return new AdNotFoundException(AD_NOT_FOUND);
                });

        Image adImage = imageService.saveAdImage(image, adEntity);
        adEntity.setImage(adImage);

        log.debug("Сохранение нового изображения объявления в базе данных для id {}.", pk);
        adRepository.save(adEntity);

        log.info("Изображение для объявления с id {} успешно обновлено.", pk);
        return adImage.getData();
    }

    public Ads getAdsMe() {
        log.info("Запрос на получение объявлений авторизованного пользователя.");
        UserEntity currentUser = userService.getCurrentUser();

        List<AdEntity> userAds = adRepository.findByAuthor(currentUser);
        Ads adsMe = new Ads()
                .setCount(userAds.size())
                .setResult(adMapper.map(userAds));

        log.info("Объявления авторизованного пользователя успешно получены. Количество: {}", adsMe.getCount());
        return adsMe;
    }

    private void logAdNotFound(int pk) {
        log.error("Ошибка: объявление с id {} не найдено!", pk);
    }
}
