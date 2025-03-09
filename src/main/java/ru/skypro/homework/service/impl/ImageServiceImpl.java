package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.enums.ImageExtension;
import ru.skypro.homework.exception.ErrorImageProcessingException;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Реализация сервиса для работы с изображениями.
 * <br> Этот класс реализует интерфейс {@link ImageService}
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${app.image.folder}")
    private String imageDir;

    private final ImageRepository imageRepository;

    /**
     * Сохранение изображения.
     *
     * @param file файл с изображением
     * @return {@link Image} новое или обновленное изображение
     * @throws ErrorImageProcessingException Если не удалось сохранение
     */
    @Override
    public Image saveImage(MultipartFile file, String namePrefix) {
        log.info("Saving an image.");
        Image image = new Image();
        String path = this.createFilePath(file, namePrefix);
        try {
            image.setPath(path)
                    .setSize((int) file.getSize())
                    .setMediaType(file.getContentType())
                    .setData(file.getBytes());
        } catch (Exception e) {
            log.error("{}. Error saving the image, the file is corrupted or unsupported!", e.getMessage());
            throw new ErrorImageProcessingException();
        }
        log.debug("Saving an image to directory: '{}'.", imageDir);
        this.saveToDir(image);
        Image savedImage = imageRepository.save(image);
        log.info("Saved an image successfully.");
        return savedImage;
    }

    /**
     * Изменение изображения.
     *
     * @param file файл с изображением
     * @param id   идентификатор изображения
     * @return {@link Image} новое или обновленное изображение
     * @throws ErrorImageProcessingException Если не удалось сохранение
     */
    @Override
    public Image updateImage(MultipartFile file, int id, String namePrefix) {
        log.info("Updating an image.");
        Image image = imageRepository.findById(id).orElse(new Image());
        String path = image.getPath() == null ? this.createFilePath(file, namePrefix) : image.getPath();
        try {
            log.warn("Data updating.");
            image.setPath(path)
                    .setSize((int) file.getSize())
                    .setMediaType(file.getContentType())
                    .setData(file.getBytes());
        } catch (IOException e) {
            log.error("{}. Error updating the image, the file is corrupted or unsupported!", e.getMessage());
            throw new ErrorImageProcessingException();
        }

        this.saveToDir(image);
        Image savedImage = imageRepository.save(image);
        log.info("Updated an image successfully.");
        return savedImage;
    }

    /**
     * Удаление изображения.
     *
     * @param imageId идентификатор изображения
     * @throws ImageNotFoundException Если изображение не найдено
     */
    @Override
    public void removeImage(int imageId) {
        log.info("Removing an image.");
        try {
            Image image =
                    imageRepository.findById(imageId)
                            .orElseThrow(ImageNotFoundException::new);
            String path = StringUtils.substringAfter(image.getPath(), "/");
            Files.deleteIfExists(Path.of(path));
            imageRepository.delete(image);
            log.info("Image successful removed!");
        } catch (Exception e) {
            log.error("Image not found!");
            throw new ErrorImageProcessingException();
        }
    }

    private String createFilePath(MultipartFile file, String namePrefix) {
        log.debug("Invoke method 'buildFilePath'");
        String fileName = DataValidator.validatedImage(file);
        String pathToFile = imageDir + namePrefix + System.currentTimeMillis() + ImageExtension.getExtension(fileName);
        return "/" + pathToFile;
    }

    private void saveToDir(Image image) {
        log.debug("Invoke method 'saveToDir'");
        String path = StringUtils.substringAfter(image.getPath(), "/");
        log.debug("Image path: {}", path);
        Path pathToImage = Path.of(path);
        try {
            if (!Files.exists(pathToImage)) {
                log.debug("Creating an directory.");
                Files.createDirectories(Path.of(imageDir));
            }
            Files.write(
                    pathToImage,
                    image.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (Exception e) {
            log.error("Error saving the image to path: '{}'", path);
            throw new ErrorImageProcessingException();
        }
    }
}
