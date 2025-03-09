package ru.skypro.homework.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.skypro.homework.dto.user.Register;

/**
 * Сервис для обработки аутентификации и регистрации пользователей.
 * <br> Этот интерфейс предоставляет методы для входа в систему (логина), регистрации нового пользователя,
 * а также очистки контекста безопасности.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);

    void clearSecurityContext(HttpServletResponse response, HttpServletRequest request);
}
