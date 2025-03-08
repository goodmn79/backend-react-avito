package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается при неверном формате файла.
 * <p>
 * Этот класс расширяет {@link RuntimeException} и автоматически вызывает ошибку с кодом статуса 400 (BAD_REQUEST) при возникновении исключения.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongFileFormatException extends RuntimeException {

    /**
     * Конструктор исключения {@link WrongFileFormatException}.
     * <br>Создаёт новое исключение без сообщения.
     */
    public WrongFileFormatException() {
    }
}
