package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается при отсутствии пользователя.
 * <p>
 * Этот класс расширяет {@link RuntimeException} и автоматически вызывает ошибку с кодом статуса 404 (NOT_FOUND) при возникновении исключения.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения {@link UserNotFoundException}.
     * <br>Создаёт новое исключение без сообщения.
     */
    public UserNotFoundException() {
    }
}
