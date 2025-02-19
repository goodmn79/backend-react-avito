package ru.skypro.homework.validate;

import org.springframework.stereotype.Component;
import ru.skypro.homework.exception.IllegalDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataValidator {
    public void validateStringLengthRange (String str, int maxLength, int minLength) throws IllegalDataException {
        if (str.length() < minLength || str.length() > maxLength) {
            throw new IllegalDataException("Длина строки должна быть от " + minLength + " до " + maxLength + " символов.");
        }
    }

    public void validatePrice (int price) throws IllegalDataException {
        if (price < 0 || price > 10_000_000) {
            throw new IllegalDataException("Цена должна быть от 0 до 10 000 000.");
        }
    }

    public void validatePhone (String phone) throws IllegalDataException {
        String pattern = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(phone);

        if (!matcher.matches()) {
            throw new IllegalDataException("Номер телефона должен соответствовать формату: +7 (XXX) XXX-XX-XX");
        }
    }




}
