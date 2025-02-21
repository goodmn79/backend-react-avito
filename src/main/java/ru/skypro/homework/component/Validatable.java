package ru.skypro.homework.component;

public interface Validatable {
    String validate(String input, int minLength, int maxLength);

    String validate(String input);

    int validate(int price);
}
