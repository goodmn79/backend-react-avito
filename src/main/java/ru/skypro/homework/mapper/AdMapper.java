package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.ad.Ad;

public class AdMapper {
    // Преобразование из AdEntity в Ad (DTO)
    public toAd(AdEntity adEntity) {
        if (adEntity == null) {
            throw new IllegalArgumentException("Объявление не может быть null");
        }
        Ad ad = new Ad();

        if (adEntity.getPk() <= 0) {
            throw new IllegalArgumentException("Некорректный ID объявления");
        }
        ad.setPk(adEntity.getPk());

        if (adEntity.getImage() == null || adEntity.getImage().isEmpty()) {
            throw new IllegalArgumentException("Изображение не может быть пустым");
        }
        ad.setImage(adEntity.getImage());

        if (adEntity.getPrice() < 0 || adEntity.getPrice() > 10000000) {
            throw new IllegalArgumentException("Цена должна быть в диапазоне от 0 до 10,000,000");
        }
        ad.setPrice(adEntity.getPrice());

        if (adEntity.getTitle() == null || adEntity.getTitle().length() < 4 || adEntity.getTitle().length() > 32) {
            throw new IllegalArgumentException("Заголовок должен содержать от 4 до 32 символов");
        }
        ad.setTitle(adEntity.getTitle());

        if (adEntity.getAuthor() == null || adEntity.getAuthor() <= 0) {
            throw new IllegalArgumentException("Автор должен быть задан корректным ID");
        }
        ad.setAuthor(adEntity.getAuthor());
        return ad;
    }

    // Преобразование из Ad (DTO) в AdEntity
    public toAdEntity(Ad ad) {
        if (ad == null) {
            throw new IllegalArgumentException("Объявление не может быть null");
        }

        AdEntity adEntity = new AdEntity();
        if (ad.getImage() == null || ad.getImage().isEmpty()) {
            throw new IllegalArgumentException("Изображение не может быть пустым");
        }
        adEntity.setImage(ad.getImage());

        if (ad.getPk() <= 0) {
            throw new IllegalArgumentException("Некорректный ID объявления");
        }
        adEntity.setPk(ad.getPk());

        if (ad.getPrice() < 0 || ad.getPrice() > 10000000) {
            throw new IllegalArgumentException("Цена должна быть в диапазоне от 0 до 10,000,000");
        }
        adEntity.setPrice(ad.getPrice());

        if (ad.getTitle() == null || ad.getTitle().length() < 4 || ad.getTitle().length() > 32) {
            throw new IllegalArgumentException("Заголовок должен содержать от 4 до 32 символов");
        }
        adEntity.setTitle(ad.getTitle());

        if (ad.getDescription() != null && (ad.getDescription().length() < 8 || ad.getDescription().length() > 64)) {
            throw new IllegalArgumentException("Описание должно содержать от 8 до 64 символов");
        }
        adEntity.setDescription(ad.getDescription());

        if (ad.getAuthor() <= 0) {
            throw new IllegalArgumentException("Автор объявления должен быть задан корректным ID");
        }
        UserEntity author = new UserEntity();
        author.setId(ad.getAuthor());
        adEntity.setAuthor(author);

        return adEntity;
    }
}
