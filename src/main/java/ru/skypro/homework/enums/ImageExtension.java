package ru.skypro.homework.enums;

import ru.skypro.homework.exception.WrongFileFormatException;

public enum ImageExtension {
    JPG(".jpg"),
    JPEG(".jpeg"),
    PNG(".png");

    private final String extension;

    ImageExtension(String extension) {
        this.extension = extension;
    }

    public static String getExtension(String extension) {
        for (ImageExtension i : ImageExtension.values()) {
            if (i.extension.equals(extension.toLowerCase())) {
                return i.extension;
            }
        }
        throw new WrongFileFormatException();
    }
}
