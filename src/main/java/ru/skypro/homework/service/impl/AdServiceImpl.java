package ru.skypro.homework.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.mapper.AdMapper;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Реализация сервиса для работы с объявлениями.
 * <br> Этот класс реализует интерфейс {@link AdService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);

    /**
     * Получение всех объявлений.
     *
     * @return {@link Ads} объект содержащий информацию об общем количестве объявлений и список всех объявлений
     */
    @Override
    public Ads getAllAds() {
        log.info("Получение всех объявлений.");

        List<AdEntity> ads = adRepository.findAll();
        log.info("Объявления успешно получены.");

        return adMapper.map(ads);
    }

    /**
     * Добавление объявления.
     *
     * @param jsonString строка с информацией объявления
     * @param image      файл с изображением объявления
     * @return объект {@link Ad} созданное объявление
     * @throws IOException Если произошла ошибка при обработке изображения
     */
    @Override
    @Transactional
    public Ad addAd(String jsonString, MultipartFile image) throws IOException {
        log.info("Создание объявления.");
        CreateOrUpdateAd createOrUpdateAd = objectMapper.readValue(jsonString, CreateOrUpdateAd.class);
        AdEntity entity = adMapper.map(createOrUpdateAd);

        entity.setAuthor(userService.getCurrentUser());

        Image savedImage = imageService.saveImage(image, entity.getPk());

        log.debug("Сохранение объявления в базе данных.");
        AdEntity ad = adRepository.save(entity.setImage(savedImage));

        log.info("Объявление успешно создано.");
        return adMapper.map(ad);
    }

    /**
     * Получение информации об объявлении.
     *
     * @param pk идентификатор объявления
     * @return объект {@link ExtendedAd} полная информация об объявлении
     */
    @Override
    public ExtendedAd getAdById(int pk) {
        log.info("Запрос на получение полного описания объявления.");

        AdEntity adEntity = this.getAdEntity(pk);

        log.info("Полное описание объявления успешно получено.");
        return adMapper.map(adEntity);
    }

    /**
     * Удаление объявления.
     *
     * @param pk идентификатор объявления
     */
    @Override
    @Transactional
    public void removeAdById(int pk) {
        log.warn("Удаление объявления.");

        AdEntity entity = this.getAdEntity(pk);
        int imageId = entity.getImage().getId();

        imageService.removeImage(imageId);
        adRepository.deleteById(entity.getPk());
        log.info("Объявление успешно удалено");
    }

    /**
     * Обновление информации об объявлении.
     *
     * @param pk               идентификатор объявления
     * @param createOrUpdateAd информация для обновления объявления
     * @return объект {@link Ad} обновленное объявление
     */
    @Override
    public Ad updateAdById(int pk, CreateOrUpdateAd createOrUpdateAd) {
        log.info("Запрос на обновление объявления.");
        AdEntity adEntity = this.getAdEntity(pk)
                .setTitle(
                        DataValidator.validatedData(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(
                        DataValidator.validatedData(createOrUpdateAd.getDescription(), 8, 64))
                .setPrice(
                        DataValidator.validatedPrice(createOrUpdateAd.getPrice()));

        log.debug("Сохранение изменений объявления в базе данных.");
        AdEntity updatedEntity = adRepository.save(adEntity);

        log.info("Объявление успешно обновлено.");
        return adMapper.map(updatedEntity);
    }

    /**
     * Обновление картинки объявления.
     *
     * @param pk    идентификатор объявления
     * @param image файл с новой картинкой объявления
     * @return объект {@link byte[]} массив байтов, содержащий данные обновлённого изображения.
     */
    @Override
    @Transactional
    public byte[] updateImage(int pk, MultipartFile image) {
        log.info("Изменение изображения объявления.");

        Image adImage = imageService.saveImage(image, pk);

        log.debug("Сохранение нового изображения объявления в базе данных.");
        adRepository.save(this.getAdEntity(pk))
                .setImage(adImage);

        log.info("Изображение для объявления успешно обновлено.");
        return adImage.getData();
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return объект {@link Ads} с информацией о количестве объявлений и их списком
     */
    @Override
    public Ads getAds() {
        log.info("Запрос на получение объявлений авторизованного пользователя.");
        UserEntity currentUser = userService.getCurrentUser();

        List<AdEntity> userAds = adRepository.findByAuthor(currentUser);
        Ads adsMe = adMapper.map(userAds);

        log.info("Объявления авторизованного пользователя успешно получены.");
        return adsMe;
    }

    /**
     * Получение информации об объявлении.
     *
     * @return объект {@link AdEntity} с информацией об объявлении
     * @throws AdNotFoundException Если объявление не найдено
     */
    @Override
    public AdEntity getAdEntity(int pk) {
        return
                adRepository.findById(pk)
                        .orElseThrow(() -> {
                            log.error("Объявление не найдено!");
                            return new AdNotFoundException();
                        });
    }

    /**
     * Проверка текущего пользователя на авторство объявления.
     *
     * @return {@code true}, если текущий пользователь является автором объявления, иначе {@code false}
     */
    @Override
    public boolean isAdAuthor(int pk, String currentUsername) {
        String adAuthorUsername = this.getAdEntity(pk).getAuthor().getUsername();
        return adAuthorUsername.equals(currentUsername);
    }
}
