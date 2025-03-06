package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

import java.io.IOException;


public interface ImageService {
    Image saveImage(MultipartFile file, int id) throws IOException;

    void removeImage(int imageId);

    String buildFileName(MultipartFile file);
}
