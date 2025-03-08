package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnsuccessImageProcessingException extends RuntimeException {
    public UnsuccessImageProcessingException() {
    }
}
