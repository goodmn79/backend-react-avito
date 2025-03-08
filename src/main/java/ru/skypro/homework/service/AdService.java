package ru.skypro.homework.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

/**
 * Сервис для работы с объявлениями.
 * <br> Этот интерфейс предоставляет методы для операций с объявлениями,
 * таких как их добавление, удаление, обновление, получение и проверка авторства.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface AdService {
    Ads getAllAds();

    @Transactional
    Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);

    ExtendedAd getAdById(int pk);

    @Transactional
    void removeAdById(int pk);

    Ad updateAdById(int pk, CreateOrUpdateAd createOrUpdateAd);

    @Transactional
    byte[] updateImage(int pk, MultipartFile image);

    Ads getAds();

    AdEntity getAdEntity(int pk);

    boolean isAdAuthor(int pk, String currentUsername);
}
