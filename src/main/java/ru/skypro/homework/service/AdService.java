package ru.skypro.homework.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(AdService.class);

    public Ads getAllAds() {
        log.info("Getting all ads...");

        List<AdEntity> ads = adRepository.findAll();
        log.info("The ads were successfully received.");

        return adMapper.map(ads);
    }

    @Transactional
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        log.info("Creating an ad...");
        AdEntity entity = adMapper.map(createOrUpdateAd);

        entity
                .setAuthor(userService.getCurrentUser())
                .setImage(this.getAdImage(entity.getPk(), image));

        log.debug("Saving an ad in a database.");
        AdEntity ad = adRepository.save(entity);

        log.info("The ad was created successfully.");
        return adMapper.map(ad);
    }

    public ExtendedAd getAdById(int pk) {
        log.info("Request to get the full description of the ad...");

        AdEntity adEntity = this.getAdEntity(pk);

        log.info("The full description of the ad was successfully received.");
        return adMapper.map(adEntity);
    }

    @Transactional
    public void removeAdById(int pk) {
        log.warn("Deleting an ad...");

        AdEntity entity = this.getAdEntity(pk);
        int imageId = entity.getImage().getId();

        imageService.removeImage(imageId);
        adRepository.deleteById(entity.getPk());
        log.info("The ad was successfully deleted.");
    }

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

    @Transactional
    public byte[] updateImage(int pk, MultipartFile image) {
        log.info("Changing the ad image.");

        Image adImage = this.getAdImage(pk, image);

        log.debug("Saving a new ad image in the database.");
        adRepository.save(this.getAdEntity(pk))
                .setImage(adImage);

        log.info("The image for the ad has been successfully updated.");
        return adImage.getData();
    }

    public Ads getAds() {
        log.info("A request to receive ads from an authorized user.");
        UserEntity currentUser = userService.getCurrentUser();

        List<AdEntity> userAds = adRepository.findByAuthor(currentUser);
        Ads adsMe = adMapper.map(userAds);

        log.info("The ads of the authorized user were successfully received.");
        return adsMe;
    }

    public AdEntity getAdEntity(int pk) {
        return
                adRepository.findById(pk)
                        .orElseThrow(() -> {
                            log.error("The ad was not found!");
                            return new AdNotFoundException();
                        });
    }

    public boolean isAdAuthor(int pk, String currentUsername) {
        String adAuthorUsername = this.getAdEntity(pk).getAuthor().getUsername();
        return adAuthorUsername.equals(currentUsername);
    }

    private Image getAdImage(int pk, MultipartFile image) {
        AdEntity adEntity = this.getAdEntity(pk);
        return adEntity.getImage() == null ?
                imageService.saveImage(image) :
                imageService.updateImage(image, adEntity.getImage().getId());
    }
}
