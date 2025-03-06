package ru.skypro.homework.component.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.IllegalDataException;
import ru.skypro.homework.exception.WrongFileFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилитарный класс для валидации входных данных.
 */
public abstract class DataValidator {
    private static final Logger log = LoggerFactory.getLogger(DataValidator.class);

    /**
     * Проверяет, что строка имеет допустимую длину.
     *
     * @param input     Входная строка
     * @param minLength Минимальная допустимая длина
     * @param maxLength Максимальная допустимая длина
     * @return Валидированная строка
     * @throws IllegalDataException Если длина строки выходит за допустимые пределы
     */
    public static String validatedData(String input, int minLength, int maxLength) {
        if (input.length() < minLength || input.length() > maxLength) {
            log.error("Длина строки не может содержать менее {} и более {} символов.", minLength, maxLength);
            throw new IllegalDataException("Длина строки должна быть от " + minLength + " до " + maxLength + " символов.");
        }
        return input;
    }

    /**
     * Проверяет формат загруженного изображения.
     *
     * @param file Файл изображения
     * @return Имя файла, если формат корректен
     * @throws WrongFileFormatException Если имя файла отсутствует
     */
    public static String validatedImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            log.error("Неверный формат изображения.");
            throw new WrongFileFormatException();
        }
        return fileName;
    }

    /**
     * Проверяет, что цена находится в допустимом диапазоне.
     *
     * @param price Цена для проверки
     * @return Валидированная цена
     * @throws IllegalDataException Если цена меньше 0 или больше 10 000 000
     */
    public static int validatedPrice(int price) {
        if (price < 0 || price > 10_000_000) {
            log.error("Цена не может быть менее {} и более {}.", 0, 10_000_000);
            throw new IllegalDataException("Цена должна быть от 0 до 10 000 000.");
        }
        return price;
    }

    /**
     * Проверяет, что номер телефона соответствует формату +7 (XXX) XXX-XX-XX.
     *
     * @param phone Номер телефона для проверки
     * @return Валидированный номер телефона
     * @throws IllegalDataException Если номер не соответствует заданному формату
     */
    public static String validatedPhoneNumber(String phone) {
        String pattern = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(phone);
        if (!matcher.matches()) {
            log.error("Номер телефона не соответствует формату +7 (XXX) XXX-XX-XX.");
            throw new IllegalDataException("Номер телефона должен соответствовать формату: +7 (XXX) XXX-XX-XX");
        }
        return phone;
    }
}
