package ru.skypro.homework.component.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.IllegalDataException;
import ru.skypro.homework.exception.WrongFileFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataValidator implements Validatable {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static String validatedData(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new WrongFileFormatException();
        }
        return fileName;
    }

    @Override
    public String validatedData(String input, int minLength, int maxLength) {
        if (input.length() < minLength || input.length() > maxLength) {
            log.error("Длина строки не может содержать менее {} и более {} символов.", minLength, maxLength);
            throw new IllegalDataException("Длина строки должна быть от " + minLength + " до " + maxLength + " символов.");
        }
        return input;
    }

    @Override
    public int validatedData(int price) {
        if (price < 0 || price > 10_000_000) {
            log.error("Цена не может быть менее {} и более {}.", 0, 10_000_000);
            throw new IllegalDataException("Цена должна быть от 0 до 10 000 000.");
        }
        return price;
    }

    @Override
    public String validatedData(String phone) {
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
