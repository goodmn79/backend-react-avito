package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

/**
 * Реализация сервиса для работы с объявлениями.
 * <br> Этот класс реализует интерфейс {@link AdService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final UserService userService;

    /**
     * Получение всех объявлений.
     *
     * @return {@link Ads} объект содержащий информацию об общем количестве объявлений и список всех объявлений
     */
    @Override
    public Ads getAllAds() {
        log.info("Getting all ads...");

        List<AdEntity> ads = adRepository.findAll();
        log.info("The ads were successfully received.");

        return adMapper.map(ads);
    }

    /**
     * Добавление объявления.
     *
     * @param createOrUpdateAd данные для создания объявления
     * @param image            файл с изображением объявления
     * @return объект {@link Ad} созданное объявление
     */
    @Override
    @Transactional
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        log.info("Creating an ad...");
        AdEntity entity = adMapper.map(createOrUpdateAd);

        entity.setImage(this.saveAdImage(image, entity.getPk()));

        log.debug("Saving an ad in a database.");
        AdEntity ad = adRepository.save(entity);

        log.info("The ad was created successfully.");
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
        log.info("Request to get the full description of the ad...");

        AdEntity adEntity = this.getAdEntity(pk);

        log.info("The full description of the ad was successfully received.");
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
        log.warn("Deleting an ad...");

        AdEntity entity = this.getAdEntity(pk);
        int imageId = entity.getImage().getId();

        imageService.removeImage(imageId);
        adRepository.deleteById(entity.getPk());
        log.info("The ad was successfully deleted.");
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
        log.info("Request to update the ad.");
        AdEntity adEntity = this.getAdEntity(pk)
                .setTitle(
                        DataValidator.validatedData(createOrUpdateAd.getTitle(), 4, 32))
                .setDescription(
                        DataValidator.validatedData(createOrUpdateAd.getDescription(), 8, 64))
                .setPrice(
                        DataValidator.validatedPrice(createOrUpdateAd.getPrice()));

        log.debug("Saving ad changes in the database.");
        AdEntity updatedEntity = adRepository.save(adEntity);

        log.info("The ad has been successfully updated.");
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
        log.info("Changing the ad image.");

        Image adImage = this.saveAdImage(image, pk);

        log.debug("Saving a new ad image in the database.");
        adRepository.save(this.getAdEntity(pk))
                .setImage(adImage);

        log.info("The image for the ad has been successfully updated.");
        return adImage.getData();
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return объект {@link Ads} с информацией о количестве объявлений и их списком
     */
    @Override
    public Ads getAds() {
        log.info("A request to receive ads from an authorized user.");
        UserEntity currentUser = userService.getCurrentUser();

        List<AdEntity> userAds = adRepository.findByAuthor(currentUser);
        Ads adsMe = adMapper.map(userAds);

        log.info("The ads of the authorized user were successfully received.");
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
                            log.error("The ad was not found!");
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

    private Image saveAdImage(MultipartFile image, int pk) {
        AdEntity adEntity;
        try {
            adEntity = this.getAdEntity(pk);
            log.info("Update ad image in the database.");
            return imageService.updateImage(image, adEntity.getImage().getId());
        } catch (Exception e) {
            log.info("Savin an ad image in a database.");
            return imageService.saveImage(image);
        }
    }
}
