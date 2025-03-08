package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Реализация сервиса для обработки аутентификации и регистрации пользователей.
 * <br> Этот класс реализует интерфейс {@link AuthService}.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder encoder;
    private final UserServiceImpl userService;

    /**
     * Выполнение процесса входа в систему.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @return {@code true}, если аутентификация прошла успешно, иначе {@code false}
     */
    @Override
    public boolean login(String username, String password) {
        log.warn("User authentication...");

        if (userService.userExists(username)) {
            UserDetails user = userDetailService.loadUserByUsername(username);
            log.info("The user has been found.");

            if (encoder.matches(password, user.getPassword())) {
                log.info("Successful user authentication.");
                return true;
            }
        }

        log.error("Failed user authentication.");
        return false;
    }

    /**
     * Регистрация нового пользователя в системе.
     *
     * @param register Объект, содержащий данные для регистрации нового пользователя
     * @return {@code true}, если регистрация прошла успешно, иначе {@code false}
     */
    @Override
    public boolean register(Register register) {
        log.info("Registering a new user...");
        if (userService.userExists(register.getUsername())) {
            log.error("The user already exists!");
            return false;
        }
        userService.addUser(register);
        log.info("User registration completed successfully.");
        return true;
    }

    /**
     * Очистка контекста безопасности.
     *
     * @param response Ответ, который будет отправлен клиенту
     * @param request  Запрос, связанный с текущей сессией пользователя
     */
    @Override
    public void clearSecurityContext(HttpServletResponse response, HttpServletRequest request) {
        log.warn("Session Termination!");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("The security context is cleared.");
        }
    }
}
