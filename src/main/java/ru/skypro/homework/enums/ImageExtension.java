package ru.skypro.homework.enums;

import ru.skypro.homework.exception.WrongFileFormatException;


/**
 * Перечисление, представляющее допустимые расширения для изображений.
 * Каждое значение перечисления соответствует расширению файла изображения.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public enum ImageExtension {
    /**
     * Расширение для файлов .jpg
     */
    JPG(".jpg"),
    /**
     * Расширение для файлов .jpeg
     */
    JPEG(".jpeg"),
    /**
     * Расширение для файлов .png
     */
    PNG(".png");

    private final String extension;

    /**
     * Конструктор для создания значения перечисления с указанным расширением.
     *
     * @param extension строка, представляющая расширение файла
     */
    ImageExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Метод для получения расширения файла по имени файла.
     *
     * @param fileName имя файла с расширением
     * @return строка с расширением файла, если оно соответствует одному из допустимых
     * @throws WrongFileFormatException если расширение файла не поддерживается
     */
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
