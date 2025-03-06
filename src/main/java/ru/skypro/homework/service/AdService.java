package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import java.io.IOException;

public interface AdService {
    Ads getAllAds();

    Ad addAd(String jsonString, MultipartFile image) throws IOException;

    ExtendedAd getAdById(int pk);

    void removeAdById(int pk);

    Ad updateAdById(int pk, CreateOrUpdateAd createOrUpdateAd);

    byte[] updateImage(int pk, MultipartFile image) throws IOException;

    Ads getAds();

    AdEntity getAdEntity(int pk);

    boolean isAdAuthor(int pk, String currentUsername);
}
