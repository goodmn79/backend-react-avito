package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

/**
 * Сервис для работы с изображениями.
 * <br> Этот интерфейс предоставляет методы для операций с изображениями (аватар пользователя и картинка объявления).
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface ImageService {
    Image saveImage(MultipartFile file, String namePrefix);

    Image updateImage(MultipartFile file, int id, String namePrefix);

    void removeImage(int imageId);
}
