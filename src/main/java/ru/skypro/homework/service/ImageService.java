package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.WrongFileFormatException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${application.images.directory}")
    private String imageDir;

    private final ImageRepository imageRepository;

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    public static final Set<String> EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");

    @Transactional
    public Image saveImage(MultipartFile file, UserEntity user) throws IOException {
        Image image = imageRepository.findByUserId(user.getId()).orElse(new Image());
        image.setPath(this.buildFileName(file, user))
                .setSize(file.getSize())
                .setMediaType(file.getContentType())
                .setData(file.getBytes())
                .setUser(user);
        this.saveToDir(image);
        return imageRepository.save(image);
    }

    private void saveToDir(Image image) throws IOException {
        String path = imageDir + image.getPath();
        Path imagePath = Path.of(path);
        Files.write(imagePath, image.getData());
        log.debug("Аватар пользователя сохранён в: '{}'", path);
    }

    private String buildFileName(MultipartFile file, UserEntity user) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new WrongFileFormatException();
        }
        String extension = getExtension(fileName);
        return user.getUsername() + "_" + user.getId() + extension;
    }

    private String getExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (!EXTENSIONS.contains(extension)) {
            throw new WrongFileFormatException();
        }
        return extension.toLowerCase();
    }
}
