package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.validation.DataValidator;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.enums.ImageExtension;
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

    @Transactional
    public Image saveImage(MultipartFile file, int id) throws IOException {
        Image image = imageRepository.findById(id).orElse(new Image());
        image.setPath(this.buildFileName(file, id))
                .setSize(file.getSize())
                .setMediaType(file.getContentType())
                .setData(file.getBytes());
        this.saveToDir(image);
        return imageRepository.save(image);
    }

    private void saveToDir(Image image) throws IOException {
        String path = imageDir + image.getPath();
        Path imagePath = Path.of(path);
        Files.write(imagePath, image.getData());
        log.debug("Изображение сохранено в: '{}'", path);
    }

    public String buildFileName(MultipartFile file, int id) {
        String fileName = DataValidator.validatedData(file);
        return id + ImageExtension.getExtension(fileName);
    }
}
