package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.enums.ImageExtension;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.exception.UnsuccessImageProcessingException;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${application.images.directory}")
    private String imageDir;

    private final ImageRepository imageRepository;

    public Image saveImage(MultipartFile file) {
        log.info("Saving an image.");
        Image image = new Image();
        String path = this.buildFilePath(file);
        try {
            image.setPath(path)
                    .setSize((int) file.getSize())
                    .setMediaType(file.getContentType())
                    .setData(file.getBytes());
        } catch (IOException e) {
            log.error("{}. Error saving the image, the file is corrupted or unsupported!", e.getMessage());
            throw new UnsuccessImageProcessingException();
        }

        this.saveToDir(image);
        Image savedImage = imageRepository.save(image);
        log.info("Saved an image successfully.");
        return savedImage;
    }

    public Image updateImage(MultipartFile file, int id) {
        log.info("Updating an image.");
        Image image = imageRepository.findById(id).orElse(new Image());
        String path = image.getPath() == null ? this.buildFilePath(file) : image.getPath();
        try {
            image.setPath(path)
                    .setSize((int) file.getSize())
                    .setMediaType(file.getContentType())
                    .setData(file.getBytes());
        } catch (IOException e) {
            log.error("{}. Error updating the image, the file is corrupted or unsupported!", e.getMessage());
            throw new UnsuccessImageProcessingException();
        }

        this.saveToDir(image);
        Image savedImage = imageRepository.save(image);
        log.info("Updated an image successfully.");
        return savedImage;
    }

    public void removeImage(int imageId) {
        log.info("Removing an image.");
        try {
            Image image =
                    imageRepository.findById(imageId)
                            .orElseThrow(ImageNotFoundException::new);
            String path = image.getPath();
            Files.deleteIfExists(Path.of(path));
            imageRepository.delete(image);
            log.info("Image successful removed!");
        } catch (Exception e) {
            log.error("Image not found!");
            throw new UnsuccessImageProcessingException();
        }
    }

    private void saveToDir(Image image) {
        log.debug("Invoke method 'saveToDir'");
        String path = image.getPath();
        log.debug("Image path: {}", path);
        Path imagePath = Path.of(path);
        try {
            if (!Files.exists(imagePath)) {
                log.debug("Creating an directory.");
                Path pathToDir = Path.of(imageDir);
                Files.createDirectories(pathToDir);
            }
            Files.write(
                    imagePath,
                    image.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (Exception e) {
            log.error("Error saving the image to path: '{}'", path);
            throw new UnsuccessImageProcessingException();
        }
    }

    public String buildFilePath(MultipartFile file) {
        log.debug("Invoke method 'buildFilePath'");
        String fileName = DataValidator.validatedImage(file);
        return imageDir + System.currentTimeMillis() + ImageExtension.getExtension(fileName);
    }
}
