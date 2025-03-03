package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.enums.ImageExtension;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${application.images.directory}")
    private String imageDir;

    private final ImageRepository imageRepository;

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    public Image saveImage(MultipartFile file, int id) throws IOException {
        log.info("Сохранение фото.");
        Image image = imageRepository.findById(id).orElse(new Image());
        String path = image.getPath() == null ? this.buildFileName(file) : image.getPath();
        image.setPath(path)
                .setSize(file.getSize())
                .setMediaType(file.getContentType())
                .setData(file.getBytes());
        this.saveToDir(image);
        return imageRepository.save(image);
    }

    public Image updateAdImage(MultipartFile file, AdEntity ad) throws IOException {
        int imageId = ad.getImage().getId();
        return this.saveImage(file, imageId);
    }

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

    private void saveToDir(Image image) throws IOException {
        String path = imageDir + image.getPath();
        Path imagePath = Path.of(path);
        Files.write(imagePath, image.getData());
        log.debug("Изображение сохранено в: '{}'", path);
    }

    public String buildFileName(MultipartFile file) {
        String fileName = DataValidator.validatedImage(file);
        return System.currentTimeMillis() + ImageExtension.getExtension(fileName);
    }
}
