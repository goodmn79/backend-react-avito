package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается при неудачном сохранении изображения.
 * <p>
 * Этот класс расширяет {@link RuntimeException} и автоматически вызывает ошибку с кодом статуса 500 (INTERNAL_SERVER_ERROR) при возникновении исключения.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErrorImageProcessingException extends RuntimeException {
    /**
     * Конструктор исключения {@link ErrorImageProcessingException}.
     * <br>Создаёт новое исключение без сообщения.
     */
    public ErrorImageProcessingException() {
    }
}
