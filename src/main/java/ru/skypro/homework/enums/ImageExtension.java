package ru.skypro.homework.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skypro.homework.exception.WrongFileFormatException;

public enum ImageExtension {
    JPG(".jpg"),
    JPEG(".jpeg"),
    PNG(".png");

    private static final Logger log = LoggerFactory.getLogger(ImageExtension.class);
    private final String extension;

    ImageExtension(String extension) {
        this.extension = extension;
    }

    public static String getExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        for (ImageExtension i : ImageExtension.values()) {
            if (i.extension.equals(extension.toLowerCase())) {
                return i.extension;
            }
        }
        throw new WrongFileFormatException();
    }
}
