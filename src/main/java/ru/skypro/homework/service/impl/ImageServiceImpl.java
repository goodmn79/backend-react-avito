package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.enums.ImageExtension;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.exception.UnsuccessfulImageSavingException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Реализация сервиса для работы с изображениями.
 * <br> Этот класс реализует интерфейс {@link ImageService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${application.images.directory}")
    private String imageDir;

    private final ImageRepository imageRepository;

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    /**
     * Сохранение изображения.
     *
     * @param file файл с изображением
     * @param id   идентификатор изображения
     * @return {@link Image} новое или обновленное изображение
     * @throws UnsuccessfulImageSavingException Если не удалось сохранение
     */
    @Override
    public Image saveImage(MultipartFile file, int id) {
        log.info("Сохранение фото.");
        Image image = imageRepository.findById(id).orElse(new Image());
        String path = image.getPath() == null ? this.buildFileName(file) : image.getPath();

        try {
            image.setPath(path)
                    .setSize((int) file.getSize())
                    .setMediaType(file.getContentType())
                    .setData(file.getBytes());
        } catch (IOException e) {
            log.error("{}. Ошибка сохранения изображения!", e.getMessage());
            throw new UnsuccessfulImageSavingException();
        }

        this.saveToDir(image);
        return imageRepository.save(image);
    }

    /**
     * Удаление изображения.
     *
     * @param imageId идентификатор изображения
     * @throws ImageNotFoundException Если изображение не найдено
     */
    @Override
    public void removeImage(int imageId) {
        try {
            Image image =
                    imageRepository.findById(imageId)
                            .orElseThrow(ImageNotFoundException::new);
            String path = imageDir + image.getPath();
            Files.deleteIfExists(Path.of(path));
            imageRepository.delete(image);
        } catch (Exception e) {
            log.error("Изображение не найдено!");
        }
    }

    /**
     * Сохранение пути файла изображения.
     *
     * @param image данные изображения
     * @throws UnsuccessfulImageSavingException Если не удалось сохранение
     */
    private void saveToDir(Image image) {
        String path = imageDir + image.getPath();
        Path imagePath = Path.of(path);
        try {
            Files.write(imagePath, image.getData());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UnsuccessfulImageSavingException();
        }
        log.debug("Изображение сохранено в: '{}'", path);
    }

    /**
     * Создание имени изображения.
     *
     * @param file файл изображения
     * @return Строка с именем изображения
     */
    @Override
    public String buildFileName(MultipartFile file) {
        String fileName = DataValidator.validatedImage(file);
        return System.currentTimeMillis() + ImageExtension.getExtension(fileName);
    }
}
