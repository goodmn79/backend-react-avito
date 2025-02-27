package ru.skypro.homework.component.validation;

public interface Validatable {
    String validatedData(String input, int minLength, int maxLength);

    String validatedData(String input);

    int validatedData(int price);
}
